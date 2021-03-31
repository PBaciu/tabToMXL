package Output;

import java.math.BigDecimal;				//Note: will need to change the duration to make it similar like the parser in master branch
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Models_Two.*;
import TabToMXL.FunctionalList;
import TabToMXL.IntermediaryGarbage2;
import generated.Attributes;
import generated.BarStyle;
import generated.Beam;
import generated.BeamValue;
import generated.Clef;
import generated.ClefSign;
import generated.Instrument;
import generated.Key;
import generated.ObjectFactory;
import generated.PartList;
import generated.PartName;
import generated.RightLeftMiddle;
import generated.ScoreInstrument;
import generated.ScorePart;
import generated.ScorePartwise;
import generated.Stem;
import generated.StemValue;
import generated.Step;
import generated.Time;
import generated.Unpitched;
import generated.Notehead;
import generated.NoteheadValue;

public class DrumParser {

	static ArrayList<String> drumTab;

	/*public static void main(String[] args) {
		drumTab = getTextArray("CC|x---------------|--------x-------|\r\n"
				+ "HH|--x-x-x-x-x-x-x-|----------------|\r\n"
				+ "SD|----o-------o---|oooo------------|\r\n"
				+ "HT|----------------|----oo----------|\r\n"
				+ "MT|----------------|------oo--------|\r\n"
				+ "BD|o-------o-------|o-------o-------|");
		
		String drumTablature = "CC|x---------------|--------x-------|\r\n"
				+ "HH|--x-x-x-x-x-x-x-|----------------|\r\n"
				+ "SD|----o-------o---|oooo------------|\r\n"
				+ "HT|----------------|----oo----------|\r\n"
				+ "MT|----------------|------oo--------|\r\n"
				+ "BD|o-------o-------|o-------o-------|";
		//System.out.println("DRUMTAB: " + drumTab);
		//System.out.println(parseDrumTab(drumTablature));
		readTab(drumTablature);

		//String converted = drumTabToXMLConversion(drumTab);

		//System.out.println(converted);
	}
	
	public static ScorePartwise readTab(String tab) {
        //TODO Determine if tab is guitar, bass, or drum
        return parseDrumTab(tab);
    }

	public static ArrayList<String> getTextArray(String text) {
		ArrayList<String> arrayText = new ArrayList<>();
		try {
			Scanner input = new Scanner(text);
			while (input.hasNextLine()) {
				arrayText.add(input.nextLine());
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayText;
	}*/
	
	public static ScorePartwise parseDrumTab(String drumTab) {
		String[] standard = {"CC", "HH", "SD", "HT", "MT", "BD"};
        List<DrumTabLine> tabLines = new ArrayList<>();
        List<DrumInstrument> device = new ArrayList<>();
        var tab = drumTab.strip();
        
        for (String letters: standard) {
        	tab = tab.replaceAll(letters, "");
        }
        
        for (var line : tab.split("\n\\s*\n")) {
        	//line = line.substring(line.indexOf("|"));
        	//line.trim();

            var lines = new FunctionalList<>(line.lines().collect(Collectors.toList()));

            AtomicInteger barDelimiterIndex = new AtomicInteger(-1);

            var subListStuff = new FunctionalList<>(lines);

            var mapped = subListStuff.flatMapIndexed((row, l) -> {
                var firstPipeIndex = l.indexOf('|');
                barDelimiterIndex.set(l.indexOf('|', 2));
                var split = new FunctionalList<>(Arrays.asList(l.substring(firstPipeIndex + 1).split("\\|")));
                var label = DrumInstrument.parse(!l.substring(0, firstPipeIndex).equals("") ? l.substring(0, firstPipeIndex): standard[row]);
                device.add(label);
                return split.mapIndexed((col, it) -> new IntermediaryGarbage2(label, row, col, it));
            });
            
            var grouped = mapped.groupBy(intermediaryGarbage2 -> intermediaryGarbage2.col);
            
            
            var bars = grouped.values().stream().map(list -> list.stream().map(intermediaryGarbage2 -> {
                var map = new HashMap<DrumInstrument, AtomicInteger>();
                var matches = Pattern.compile("([-ox]{1})")//* means anything 
                        .matcher(intermediaryGarbage2.val)
                        .results()
                        .map(MatchResult::group);

                return matches.map(match -> {		//unsure if correct         	
                	
                	 //System.out.println(match);
                	//an idea, maybe matches can be a group like oooo but match is forced to take only 1 o due to .matches() so we need to loop it?
                	//But look at the test.java though, matcher stores the found matches
                    if (match.matches("[ox]")) {	//should only match with either o or x
                        int prev = intermediaryGarbage2.val.indexOf(match,map.getOrDefault(intermediaryGarbage2.label, new AtomicInteger(0)).get());
                        map.put(intermediaryGarbage2.label, new AtomicInteger(prev + 1));
                        return new DrumNote(match, intermediaryGarbage2.label, intermediaryGarbage2.col,  prev);
                    } else {
                        return new DrumNote(null, null, intermediaryGarbage2.col, intermediaryGarbage2.val.indexOf(match, map.getOrDefault(intermediaryGarbage2.label, new AtomicInteger(0)).get()));
                    }
                    //TODO Handle cases of mixed hammeron, pullofs, bends and slides

                }).collect(Collectors.groupingBy(note -> note.inBar));
            }).collect(Collectors.toList())).collect(Collectors.toCollection(ArrayList::new));   //bars measure 2 line 3 (3), line 4 (1), 5 (1)
            
            List<DrumBar> barModelList = new ArrayList<>();		//note barModelList does not give right bar length
            for(int i = 0; i < bars.size(); i++) {
                List<DrumNote> notes = new ArrayList<>();
                for (var note : bars.get(i)){
                    notes.addAll(note.get(i));
                }
                int barLength = notes.size() / 6;
                
                //System.out.println("barlength: " + barLength);		//correct 16
                //unsure
                notes = notes.parallelStream().filter(note -> Objects.nonNull(note.value)).collect(Collectors.toList());
                DrumBar bar = new DrumBar(notes, barLength);

                barModelList.add(bar);
            }
            List<DrumBar> barList = new ArrayList<>();
            for (var bar: barModelList) {
                PriorityQueue<DrumNote> pq = new PriorityQueue<>(bar.notes);
                List<DrumNote> notes = new ArrayList<>();
                while (!pq.isEmpty()) {
                    notes.add(pq.poll());
                }
                DrumBar b = new DrumBar(notes, bar.barLength);
                barList.add(b);
            }
            //System.out.println("measure0: " + barList.get(0).notes.size() + "\tmeasure1: " + barList.get(1).notes.size());
            
            for (var bar: barList) {
            	for (var note: bar.notes) {
            		//System.out.println("note: " + note);
            	}
            }
            DrumTabLine tabLine = new DrumTabLine(barList);
            tabLines.add(tabLine);
            
        }
        Collections.reverse(device);
		return generateDrumXML(new DrumTab(tabLines, device));
	}
	
	public static ScorePartwise generateDrumXML(DrumTab drumTab) {
		ObjectFactory factory = new ObjectFactory();
		
		ScorePartwise scorePartwise = factory.createScorePartwise();
        scorePartwise.setVersion("3.1");
        PartList partList = factory.createPartList();
        ScorePart scorePart = factory.createScorePart();
        scorePart.setId("P1");
        
        PartName partName = factory.createPartName();
        partName.setValue("Drumset");
        scorePart.setPartName(partName);		//how to set score-instrument?
        
        ArrayList<String> InstrumentNames = new ArrayList<String>(Arrays.asList("Bass Drum 1", "Bass Drum 2",
				"Side Stick", "Snare", "Low Floor Tom", "Closed Hi-Hat", "High Floor Tom", "Pedal Hi-Hat",
				"Low Tom", "Open Hi-Hat", "Low-Mid Tom", "Hi-Mid Tom", "Crash Cymbal 1", "High Tom",
				"Ride Cymbal 1", "Chinese Cymbal", "Ride Bell", "Tambourine", "Splash Cymbal", "Cowbell",
				"Crash Cymbal 2", "Ride Cymbal 2", "Open Hi Conga", "Low Conga"));
		ArrayList<Integer> ScoreInstrumentID = new ArrayList<Integer>(Arrays.asList(36, 37, 38, 39, 42, 43, 44, 45,
				46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 60, 64, 65));
		
		for (int i = 0; i < InstrumentNames.size(); i++) {			
			ScoreInstrument instruments = factory.createScoreInstrument();
			instruments.setId("P1-I" + ScoreInstrumentID.get(i));
			instruments.setInstrumentName(InstrumentNames.get(i));
			scorePart.getScoreInstrument().add(instruments);
		}
        
        
        partList.getPartGroupOrScorePart().add(scorePart);
        scorePartwise.setPartList(partList);
        
        ScorePartwise.Part part = new ScorePartwise.Part();
        part.setId(scorePart);
                
        double counter = 0; 
        int currMeasure = 0;
        for (var line : drumTab.tabLines) {
            for (int i = 0; i < line.bars.size(); i++) {
                int quarterNoteLength = 4;
                ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
                measure.setNumber(Integer.toString(currMeasure + 1));
                Attributes attributes = new Attributes();
                attributes.setDivisions(new BigDecimal(line.bars.get(i).barLength / quarterNoteLength));
                Key key = new Key();
                key.setFifths(BigInteger.valueOf(0));
                attributes.getKey().add(key);
                Clef clef = new Clef();
                ClefSign cf = ClefSign.PERCUSSION;
                clef.setSign(cf);
                clef.setLine(BigInteger.valueOf(5));
                attributes.getClef().add(clef);

                Time time = factory.createTime();
                var beats = factory.createTimeBeats(Integer.toString(4));
                var beatType = factory.createTimeBeatType(Integer.toString(4));
                time.getTimeSignature().add(beats);
                time.getTimeSignature().add(beatType);
                attributes.getTime().add(time);					
                
                if (i == 0) {
                    measure.getNoteOrBackupOrForward().add(attributes);				//got attributes
                }         
                
                DoublyLinkedList list = new DoublyLinkedList();
                int noteIndex = 0;												//notes start here
                var distanceMap = new HashMap<Integer, List<DrumNote>>();
                for (var n : line.bars.get(i).notes) {
                	                	
                	//System.out.println(line.bars.get(0));	//measure 0
                	//System.out.println(line.bars.get(1));	//measure 1`
                    distanceMap.putIfAbsent(n.absoluteDistance, new ArrayList<>());
                    distanceMap.get(n.absoluteDistance).add(n);
                    
                    generated.Note note = new generated.Note();

                    if (distanceMap.get(n.absoluteDistance).size() > 1) {
                        note.setChord(factory.createEmpty());
                    }

                    double duration;
                    if (line.bars.get(i).notes.stream().noneMatch(note1 -> note1.absoluteDistance > n.absoluteDistance)) {
                        duration = (double)(line.bars.get(i).barLength  - line.bars.get(i).notes.get(noteIndex).absoluteDistance) / quarterNoteLength;
                    }
                    else {
                        duration = (double)(line.bars.get(i).notes.get(noteIndex + 1).absoluteDistance - line.bars.get(i).notes.get(noteIndex).absoluteDistance) / quarterNoteLength;
                    }
                    if (duration == 0) {
                        var tempIndex = noteIndex;
                        while (tempIndex < line.bars.get(i).notes.size()) {
                            if (line.bars.get(i).notes.get(tempIndex).absoluteDistance == n.absoluteDistance) {
                                tempIndex++;
                            } else {
                                duration = (double)(line.bars.get(i).notes.get(tempIndex).absoluteDistance - n.absoluteDistance) / quarterNoteLength;
                                break;
                            }
                        }
                    }

                    String noteTypeString;
                    if (duration == 1.0) {
                        noteTypeString = "quarter";
                    } else if (duration == 0.5) {
                        noteTypeString = "eighth";
                        Beam beam = new Beam();
                        note.getBeam().add(beam);
                    } else if (duration == 0.25) {
                        noteTypeString = "sixteenth";
                        Beam beam1 = new Beam();
                        note.getBeam().add(beam1);
                        Beam beam2 = new Beam();
                        note.getBeam().add(beam2);
                    } else if (duration == 2) {
                        noteTypeString = "half";
                    } else if (duration == 4) {
                        noteTypeString = "whole";
                    }
                    else {
                        noteTypeString = "quarter";
                    }

                    var noteType = factory.createNoteType();
                    noteType.setValue(noteTypeString);
                    note.setType(noteType);
                    note.setDuration(BigDecimal.valueOf(duration));

                    
                    
                    if (n.value.equals("x")) {				//== does not works
                    	note.setVoice("1");
                    	Notehead head = new Notehead();
                    	note.setNotehead(head);
                    	note.getNotehead().setValue(NoteheadValue.X);
                    	note.setStem(new Stem());
                    	note.getStem().setValue(StemValue.UP);			//what if its a whole note? later I guess
                    } else if (n.value.equals("o")) {
                    	note.setVoice("2");
                    	note.setStem(new Stem());
                    	note.getStem().setValue(StemValue.DOWN);			//got voice, duration, type, notehead, stem
                    }
                    										//need instrument, unpitched
                    list.addNode(note);
                    
                    if (n.value.equals("x") && duration < 1) {
                    	if (counter == 0) {
                    		counter = counter + duration;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.BEGIN);
                    		}
                    	} else if ((counter + duration) == 1) {
                    		counter = 0;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.END);
                    		}
                    	} else {
                    		counter = counter + duration;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.CONTINUE);
                    		}
                    	}
                    }
                    
                    if (n.value.equals("o") && duration < 1) {
                    	if (counter == 0) {
                    		counter = counter + duration;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.BEGIN);
                    		}
                    	} else if ((counter + duration) == 1) {
                    		counter = 0;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.END);
                    		}
                    	} else {
                    		counter = counter + duration;
                    		for (var beam: note.getBeam()) {
                    			beam.setValue(BeamValue.CONTINUE);
                    		}
                    	}
                    }
                    
                    //String instruName = n.instrument.name().replaceAll("(.)([A-Z])", "$1 $2").replaceAll("Hi Hat", "Hi-Hat").replaceAll("Low Mid", "Low-Mid").replaceAll("Hi Mid", "Hi-Mid");//.replaceAll("(.)([A-Z])", "$1 $2");
                    // [com.sun.istack.SAXException2: Object "P1-I43" is found in an IDREF property but this object doesnt have an ID.]
                    
                    Instrument instru = new Instrument();
                    Unpitched variable = new Unpitched();  
                    
                    switch (n.instrument.toString().replaceAll("(.)([A-Z])", "$1 $2")) {
                    	case "Hi Hat":
                    		variable.setDisplayStep(Step.G);
                    		variable.setDisplayOctave(5);
                    		if (n.value.equals("x")) {
                    			instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Closed Hi-Hat")));
                    		} else if (n.value.equals("o")) {
                    			instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Open Hi-Hat")));
                    		}
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		break;
                    	
                    	case "Bass Drum":
                    	case "Kick Drum":
                    		variable.setDisplayStep(Step.F);
                    		variable.setDisplayOctave(4);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Bass Drum 1")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		break;
                    		
                    	case "Snare Drum":
                    		variable.setDisplayStep(Step.C);
                    		variable.setDisplayOctave(5);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Snare")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		break;
                    		
                    	case "Ride Cymbal":
                    		variable.setDisplayStep(Step.F);
                    		variable.setDisplayOctave(5);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Ride Cymbal 1")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		break;
                    		
                    	case "Crash Cymbal":
                    		variable.setDisplayStep(Step.A);
                    		variable.setDisplayOctave(5);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Crash Cymbal 1")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		break;
                    		
                    	case "High Tom":
                    		variable.setDisplayStep(Step.E);
                    		variable.setDisplayOctave(5);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Low-Mid Tom")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		break;
                    		
                    	case "Medium Tom":
                    		variable.setDisplayStep(Step.D);
                    		variable.setDisplayOctave(5);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Low Tom")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		break;
                    		
                    	case "Floor Tom":
                    		variable.setDisplayStep(Step.A);
                    		variable.setDisplayOctave(4);
                    		instru.setId(scorePart.getScoreInstrument().get(InstrumentNames.indexOf("Low Floor Tom")));
                    		note.setInstrument(instru);
                    		note.setUnpitched(variable);
                    		break;
                    	
                    }
                    
                    measure.getNoteOrBackupOrForward().add(note);
                    noteIndex++;
                }
                //list.printNodes();
                //System.out.println(measure.getNoteOrBackupOrForward().toString());
                part.getMeasure().add(measure);
                currMeasure++;
            }
        }
        var bl = factory.createBarline();
        var styleColor = factory.createBarStyleColor();
        BarStyle bs = BarStyle.HEAVY;
        styleColor.setValue(bs);
        bl.setLocation(RightLeftMiddle.RIGHT);
        bl.setBarStyle(styleColor);
        part.getMeasure().get(currMeasure - 1).setBarline(bl);
        scorePartwise.getPart().add(part);

        return scorePartwise;
	}
}
