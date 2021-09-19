package com.endava.internship.collections;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;


public class StudentSet implements Set<Student> {
    private Node root;
    private int size;
    private Comparator<Student> Comparator;
    private boolean markForContainsDelete;

    public StudentSet(Comparator<Student> Comparator) {
        this.Comparator = Comparator;
    }

    StudentSet() {
    }

    private void inorderRec(Node root, ArrayList<Student> studentsNode) {
        if (root != null) {
            inorderRec(root.left, studentsNode);
            studentsNode.add(root.value);
            inorderRec(root.right, studentsNode);
        }
    }

    private Node insertNode(Node root, Student student) {
        if (root == null) {
            root = new Node(student);
        }
        int comp;
        if (Comparator != null) {
            comp = Comparator.compare(student, root.value);
        } else {
            comp = student.compareTo(root.value);
        }

        if (comp < 0) {
            root.left = insertNode(root.left, student);
        } else if (comp > 0) {
            root.right = insertNode(root.right, student);
        }
        return root;
    }

    static boolean findNode(Node root, Student student) {
        if (root == null)
            return false;
        if (student.compareTo(root.value) == 0)
            return true;
        boolean res1 = findNode(root.left, student);
        if (res1) return true;
        return findNode(root.right, student);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size < 1;
    }

    @Override
    public boolean contains(Object o) {
        Student s = (Student) o;
        return findNode(root, s);
    }

    @Override
    public Iterator<Student> iterator() {
        class BinarySearchIterator implements Iterator<Student> {
            int i = 0;
            private ArrayList<Student> studentsNode = new ArrayList<>();

            public BinarySearchIterator() {
                inorderRec(root, studentsNode);
            }

            @Override
            public boolean hasNext() {
                return size > i;
            }

            @Override
            public Student next() {
                Student st = studentsNode.get(i);
                i++;
                return st;
            }
        }
        return new BinarySearchIterator();
    }

    @Override
    public Object[] toArray() {
        int i=0;
        Object[] results = new Object[size];
        ArrayList arrayList = new ArrayList();
        inorderForToArray(root,arrayList);
        for (Object o:arrayList){
            results[i++] = o;
        }
        return results;
    }

    private void inorderForToArray(Node root, ArrayList studentsNode) {
        if (root != null) {
            inorderRec(root.left, studentsNode);
            studentsNode.add(root.value);
            inorderRec(root.right, studentsNode);
        }
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        ArrayList<T> result = new ArrayList();
        inorderForToArray(root, result);
        int k = 0;
        for (T t : result) {
            ts[k] = t;
            k++;
        }
        return ts;
    }

    @Override
    public boolean add(Student student) {
        if (findNode(root, student)) {
            return false;
        } else {
            root = insertNode(root, student);
            size++;
        }
        return true;
    }

    private Student minValue(Node node) {
        Student minStudent = root.value;
        while (root.left != null) {
            minStudent = root.left.value;
            root = root.left;
        }
        return minStudent;
    }

    private Node deleteNode(Node root, Student student) {
        if (root == null) {
            return null;
        }
        markForContainsDelete = false;
        int comp;
        if (Comparator != null) {
            comp = Comparator.compare(student, root.value);
        } else {
            comp = student.compareTo(root.value);
        }
        if (comp < 0) {
            root.left = deleteNode(root.left, student);
        } else if (comp > 0) {
            root.right = deleteNode(root.right, student);
        } else {
            markForContainsDelete = true;
            if (root.left == null){
                return root.right;
            }
            else if (root.right == null){
                return root.left;
            }
            root.value = minValue(root.right);
            root.right = deleteNode(root.right, root.value);
        }
        return root;
    }

    @Override
    public boolean remove(Object o) {
        Student st = (Student) o;
        root = deleteNode(root, st);

        if (markForContainsDelete) {
            size--;
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString()
    {
        StringBuffer string = new StringBuffer();
        string.append('[');
        inorderPrint(root, string,0);
        string.append(']');
        return string.toString();
    }

    private void inorderPrint(Node root, StringBuffer stringBuffer,int index)
    {
        index++;
        if (root != null)
        {
            inorderPrint(root.left, stringBuffer,index);
            stringBuffer.append(root.value.toString());
            if(index!=size){
                stringBuffer.append(", ");
            }
            inorderPrint(root.right, stringBuffer,index);
        }
    }

    @Override
    public boolean addAll(Collection<? extends Student> collection) {

        for (Student s : collection) {
            add(s);
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean removeAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    class Node {
        Student value;
        Node left;
        Node right;

        public Node(Student item) {
            value = item;
        }
    }
}