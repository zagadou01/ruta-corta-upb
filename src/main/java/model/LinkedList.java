package model;

// Lista Enlazada genérica para implementar la lista de vértices y de sitios
public class LinkedList <E extends Structure> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void add(E element) {
        Node<E> node = new Node<>(element);
        if (head != null) {
            Node<E> auxiliar = head;
            while(auxiliar.getNext() != null) {
                auxiliar = auxiliar.getNext();
            }
            auxiliar.setNext(node);
        } else {
            head = node;
        }
        tail = node;
        node.getElement().setIndex(size);
        size++;
    }

    public void remove(int index) {
        if (head != null) {
            if (head.getElement().getIndex() == index) {
                head = head.getNext();
                size--;
            } else {
                Node<E> auxiliar = searchPrevious(index);
                if (auxiliar != null) {
                    auxiliar.setNext(auxiliar.getNext().getNext());
                    size--;
                }
            }
        }
    }

    public void remove(String name) {
        if (head != null) {
            if (head.getElement().getName().equals(name)) {
                head = head.getNext();
                size--;
            } else {
                Node<E> auxiliar = searchPrevious(name);
                if (auxiliar != null) {
                    auxiliar.setNext(auxiliar.getNext().getNext());
                    size--;
                }
            }
        }
    }

    public int getIndex(String name) {
        Node<E> auxiliar = searchCurrent(name);
        if (auxiliar != null) {
            return auxiliar.getElement().getIndex();
        }
        return -1;
    }

    public String getName(int index) {
        Node<E> auxiliar = searchCurrent(index);
        if (auxiliar != null) {
            return auxiliar.getElement().getName();
        }
        return null;
    }

    public E getNode(String name) {
        Node<E> auxiliar = searchCurrent(name);
        if (auxiliar != null) {
            return auxiliar.getElement();
        }
        return null;
    }

    public E getNode(int index) {
        Node<E> auxiliar = searchCurrent(index);
        if (auxiliar != null) {
            return auxiliar.getElement();
        }
        return null;
    }

    public E getHead() {
        return head.getElement();
    }

    public E getTail() {
        return tail.getElement();
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        if (head != null) {
            Node<E> auxiliar = head;
            while (auxiliar != null) {
                text.append(auxiliar.getElement().getName());
                auxiliar = auxiliar.getNext();
                if (auxiliar != null) {
                    text.append(";");
                }
            }
        }
        return text.toString();
    }

    private Node<E> searchCurrent(int index) {
        if (head != null) {
            Node<E> auxiliar = head;
            while (auxiliar != null) {
                if (auxiliar.getElement().getIndex() == index) {
                    return auxiliar;
                }
                auxiliar = auxiliar.getNext();
            }
        }
        return null;
    }

    private Node<E> searchCurrent(String name) {
        if (head != null) {
            Node<E> auxiliar = head;
            while (auxiliar != null) {
                if (auxiliar.getElement().getName().equals(name)) {
                    return auxiliar;
                }
                auxiliar = auxiliar.getNext();
            }
        }
        return null;
    }

    private Node<E> searchPrevious(int index) {
        if (head != null) {
            Node<E> auxiliar = head;
            while (auxiliar.getNext() != null) {
                if (auxiliar.getNext().getElement().getIndex() == index) {
                    return auxiliar;
                }
                auxiliar = auxiliar.getNext();
            }
        }
        return null;
    }

    private Node<E> searchPrevious(String name) {
        if (head != null) {
            Node<E> auxiliar = head;
            while (auxiliar.getNext() != null) {
                if (auxiliar.getNext().getElement().getName().equals(name)) {
                    return auxiliar;
                }
                auxiliar = auxiliar.getNext();
            }
        }
        return null;
    }

    // clase nodo genérica para implementar como elementos los hijos de estructura
    private static class Node <E extends Structure> {
        private E element;
        private Node<E> next;

        public Node(E element) {
            this.element = element;
        }

        public E getElement() {
            return element;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }
    }
}
