import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String author;
    private boolean available;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "tytul " + title + ", Autor: " + author + ", Dostepna? " + available;
    }
}

class Library implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
        addBook(new Book("Harry Potter", "J.K. Rowling"));
        addBook(new Book("To Kill a Mockingbird", "Harper Lee"));
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void displayBooks() {
        System.out.println("Wszystkie ksiazki:");
        for (Book book : books) {
            System.out.println(book.toString());
        }
    }

    public boolean borrowBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && book.isAvailable()) {
                book.setAvailable(false);
                System.out.println("ksiazka '" + title + "' zostala wypozyczona.");
                return true;
            }
        }
        System.out.println("ksiazka '" + title + "' nie jest dostepna.");
        return false;
    }

    public void saveLibraryToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
            System.out.println("Zapisano do pliku: " + fileName);
        } catch (IOException e) {
            System.err.println("Error przy zapisywaniu: " + e.getMessage());
        }
    }

    public static Library loadLibraryFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Library) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Plik nie istnieje lub błąd wczytywania: " + e.getMessage());
            return new Library();
        }
    }
}

public class LibraryApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library;
        System.out.print("Wprowadz nazwe pliku: ");
        String fileName = scanner.nextLine();
        library = Library.loadLibraryFromFile(fileName);

        while (true) {
            System.out.println("\n---Menu Biblioteki---:");
            System.out.println("1. Dodaj ksiazke");
            System.out.println("2. Wyswietl ksiazki");
            System.out.println("3. Wypozycz ksiazke");
            System.out.println("4. Wyjdz i zapisz");

            System.out.print("Wybierz numerek: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Podaj tytul: ");
                    String title = scanner.nextLine();
                    System.out.print("Podaj Autora: ");
                    String author = scanner.nextLine();
                    Book newBook = new Book(title, author);
                    library.addBook(newBook);
                    System.out.println("Dodano.");
                    break;

                case 2:
                    library.displayBooks();
                    break;

                case 3:
                    System.out.print("Podaj tytul ksiazki do wypozyczenia: ");
                    String borrowTitle = scanner.nextLine();
                    library.borrowBook(borrowTitle);
                    break;

                case 4:
                    library.saveLibraryToFile(fileName);
                    System.out.println("Zapisywanie");
                    System.exit(0);

                default:
                    System.out.println("Niepoprawny wybor");
            }
        }
    }
}
