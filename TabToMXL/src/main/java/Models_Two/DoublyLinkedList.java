package Models_Two;

import generated.Note;
import generated.*;

public class DoublyLinkedList {    
    class Node{  
        Note item;  
        Node previous;  
        Node next;  
   
        public Node(Note note) {  
            this.item = note;  
        }  
    }  
    Node head, tail = null;  
   
    public void addNode(Note note) {  
        Node newNode = new Node(note);  
        if (head == null) {  
            head = tail = newNode;  
            head.previous = null;  
            tail.next = null;  
        } else {  
            tail.next = newNode;  
            newNode.previous = tail;  
            tail = newNode;  
            tail.next = null;  
        }  
    }  
    
    public void setBeam() {
    	Node current = head;
    }
   
    public void printNodes() {  
        Node current = head;  
        if (head == null) {  
            return;  
        }  
        System.out.println();
        System.out.print("Here: ");
        while (current != null) {  
            System.out.print(current.item.getInstrument() + " ");  
            current = current.next;  
        }  
    }  
}