import java.io.*;
import java.util.*;

abstract class Pet {
    protected String name;
    protected int age;
    protected String colour;
    protected double weight;

    public Pet(String name, int age, String colour, double weight) {
        this.name = name;
        this.age = age;
        this.colour = colour.toLowerCase();
        this.weight = weight;
    }

    public abstract String speak();

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getColour() { return colour; }
    public double getWeight() { return weight; }

    public void setName(String n) { name = n; }
    public void setAge(int a) { age = a; }
    public void setColour(String c) { colour = c.toLowerCase(); }
    public void setWeight(double w) { weight = w; }
}

class Dog extends Pet {
    private String breed;

    public Dog(String name, int age, String colour, double weight, String breed) {
        super(name, age, colour, weight);
        this.breed = breed;
    }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    @Override
    public String speak() {
        return "Woof! I am " + name + ", a " + age + " year old " + breed;
    }
}

class Cat extends Pet {
    private String breed;

    public Cat(String name, int age, String colour, double weight, String breed) {
        super(name, age, colour, weight);
        this.breed = breed;
    }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    @Override
    public String speak() {
        return "Meow! I am " + name + ", a " + age + " year old " + breed;
    }
}

public class PetClinic {

    static final String CLINIC_NAME = "Happy Tails Clinic";
    static final String PET_FILE = "src/PetDetails";

    static ArrayList<Pet> pets = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        loadPetsFromFile();

        int option = -1;

        while (option != 7) {
            System.out.println("\n--- Welcome to " + CLINIC_NAME + " ---");
            System.out.println("1. Add Pet");
            System.out.println("2. Delete Pet");
            System.out.println("3. Modify Pet");
            System.out.println("4. View Pets");
            System.out.println("5. Report");
            System.out.println("6. Search Pet");
            System.out.println("7. Exit");

            System.out.print("Select an option (1-7): ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid number.");
                continue;
            }

            switch (option) {
                case 1 -> addPet(scanner);
                case 2 -> deletePet(scanner);
                case 3 -> modifyPet(scanner);
                case 4 -> viewPets();
                case 5 -> generateReport();
                case 6 -> searchPet(scanner);
                case 7 -> saveAndExit();
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void addPet(Scanner sc) {
        String type;

        while (true) {
            System.out.print("Enter pet type (dog/cat): ");
            type = sc.nextLine().toLowerCase();
            if (type.equals("dog") || type.equals("cat")) break;
            System.out.println("Invalid pet type.");
        }

        System.out.print("Name: ");
        String name = sc.nextLine();

        int age;
        while (true) {
            System.out.print("Age: ");
            try {
                age = Integer.parseInt(sc.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid age.");
            }
        }

        System.out.print("Colour: ");
        String colour = sc.nextLine();

        double weight;
        while (true) {
            System.out.print("Weight: ");
            try {
                weight = Double.parseDouble(sc.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid weight.");
            }
        }

        System.out.print("Breed: ");
        String breed = sc.nextLine();

        if (type.equals("dog")) pets.add(new Dog(name, age, colour, weight, breed));
        else pets.add(new Cat(name, age, colour, weight, breed));

        savePetsToFile();
        System.out.println("Pet added. Autosave successful!");
    }

    static void deletePet(Scanner sc) {
        System.out.print("Enter name of pet to delete: ");
        String name = sc.nextLine();

        boolean removed = pets.removeIf(p -> p.getName().equalsIgnoreCase(name));

        if (removed) {
            savePetsToFile();
            System.out.println("Pet deleted.");
        } else {
            System.out.println("Pet not found.");
        }
    }

    static void modifyPet(Scanner sc) {
        System.out.print("Enter pet name to modify: ");
        String name = sc.nextLine();

        Pet pet = null;
        for (Pet p : pets) {
            if (p.getName().equalsIgnoreCase(name)) {
                pet = p;
                break;
            }
        }

        if (pet == null) {
            System.out.println("Pet not found.");
            return;
        }

        System.out.println("1. Name\n2. Colour\n3. Age\n4. Weight\n5. Breed");
        int choice;

        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid option.");
            return;
        }

        switch (choice) {
            case 1 -> {
                System.out.print("New name: ");
                pet.setName(sc.nextLine());
            }
            case 2 -> {
                System.out.print("New colour: ");
                pet.setColour(sc.nextLine());
            }
            case 3 -> {
                System.out.print("New age: ");
                try {
                    pet.setAge(Integer.parseInt(sc.nextLine()));
                } catch (Exception e) {
                    System.out.println("Invalid age.");
                }
            }
            case 4 -> {
                System.out.print("New weight: ");
                try {
                    pet.setWeight(Double.parseDouble(sc.nextLine()));
                } catch (Exception e) {
                    System.out.println("Invalid weight.");
                }
            }
            case 5 -> {
                System.out.print("New breed: ");
                String breed = sc.nextLine();
                if (pet instanceof Dog d) d.setBreed(breed);
                if (pet instanceof Cat c) c.setBreed(breed);
            }
            default -> System.out.println("Invalid choice.");
        }

        savePetsToFile();
        System.out.println("Pet updated.");
    }

    static void viewPets() {
        if (pets.isEmpty()) {
            System.out.println("No pets to display.");
            return;
        }

        for (Pet p : pets) {
            System.out.println(p.speak());
        }
    }

    static void generateReport() {
        long dogs = pets.stream().filter(p -> p instanceof Dog).count();
        long cats = pets.stream().filter(p -> p instanceof Cat).count();

        System.out.println("Total Dogs: " + dogs);
        System.out.println("Total Cats: " + cats);
    }

    static void searchPet(Scanner sc) {
        System.out.println("Search by:\n1. Name\n2. Weight\n3. Age\n4. Colour\n5. Breed");

        int option;

        try {
            option = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid option.");
            return;
        }

        System.out.print("Enter search value: ");
        String query = sc.nextLine().toLowerCase();

        boolean found = false;

        for (Pet p : pets) {
            switch (option) {
                case 1 -> {
                    if (p.getName().equalsIgnoreCase(query)) {
                        System.out.println(p.speak());
                        found = true;
                    }
                }
                case 2 -> {
                    try {
                        if (p.getWeight() == Double.parseDouble(query)) {
                            System.out.println(p.speak());
                            found = true;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid weight.");
                        return;
                    }
                }
                case 3 -> {
                    try {
                        if (p.getAge() == Integer.parseInt(query)) {
                            System.out.println(p.speak());
                            found = true;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid age.");
                        return;
                    }
                }
                case 4 -> {
                    if (p.getColour().equalsIgnoreCase(query)) {
                        System.out.println(p.speak());
                        found = true;
                    }
                }
                case 5 -> {
                    if (p instanceof Dog d && d.getBreed().equalsIgnoreCase(query)) {
                        System.out.println(d.speak());
                        found = true;
                    }
                    if (p instanceof Cat c && c.getBreed().equalsIgnoreCase(query)) {
                        System.out.println(c.speak());
                        found = true;
                    }
                }
            }
        }

        if (!found) System.out.println("No pets found.");
    }

    static void savePetsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PET_FILE))) {
            for (Pet p : pets) {
                String type = (p instanceof Dog) ? "dog" : "cat";
                String breed = (p instanceof Dog d) ? d.getBreed() : ((Cat) p).getBreed();
                bw.write(type + "," + p.getName() + "," + p.getAge() + "," + p.getColour() + "," + p.getWeight() + "," + breed);
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error saving pets.");
        }
    }

    static void loadPetsFromFile() {
        pets.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(PET_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 6) continue;

                String type = parts[0].trim().toLowerCase();
                String name = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                String colour = parts[3].trim().toLowerCase();
                double weight = Double.parseDouble(parts[4].trim());
                String breed = parts[5].trim();

                if (type.equals("dog")) pets.add(new Dog(name, age, colour, weight, breed));
                if (type.equals("cat")) pets.add(new Cat(name, age, colour, weight, breed));
            }
        } catch (Exception e) {
            System.out.println("Error loading pets.");
        }
    }

    static void saveAndExit() {
        savePetsToFile();
        System.out.println("Exiting...");
        System.exit(0);
    }
}
