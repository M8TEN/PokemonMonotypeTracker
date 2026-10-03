package Server.DatabaseAccessObjects;

import Battle.Moves.DamageMove;
import Shared.PokemonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class MoveDataAdder {
    public static void main (String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Mydatabase.db")) {
            MoveDAO moveDAO = new MoveDAO(conn);
            boolean run = true;
            Scanner input = new Scanner(System.in);
            while (run) {
                System.out.print("Command: ");
                String command = input.nextLine().strip().toLowerCase();
                System.out.println();
                switch (command) {
                    case "add": {
                        addMove(moveDAO, input);
                        break;
                    }
                    case "exit": run = false; break;
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMove(MoveDAO dao, Scanner in) {
        System.out.print("Move Type: ");
        String addType = in.nextLine().strip().toLowerCase();
        System.out.println();
        System.out.print("Move Name: ");
        String moveName = in.nextLine().strip();
        System.out.println();
        int maxPP = getNumber(in, "MaxPP");
        int accuracy = getNumber(in, "Accuracy");
        PokemonType moveType = getType(in);
        switch (addType) {
            case "damage": {
                int damage = getNumber(in, "Damage");
                DamageMove nextMove = new DamageMove(-1, moveName, moveType.ordinal(), maxPP, accuracy, damage);
                try {
                    dao.createEntry(nextMove, moveName);
                }
                catch (Exception e) {
                    System.out.println("Could not add new damage move");
                }
                break;
            }

            default: {
                System.out.println("Move Type " + moveType + " is not supported yet");
                return;
            }
        }
    }

    private static int getNumber(Scanner in, String numName) {
        boolean success = false;
        int num = 0;
        while (!success) {
            try {
                System.out.print(numName + ": ");
                num = Integer.parseInt(in.nextLine().strip());
                System.out.println();
                success = true;
            }
            catch (Exception e) {
                System.out.println("Invalid Input, please provide an Integer");
            }
        }
        return num;
    }

    private static PokemonType getType(Scanner in) {
        boolean success = false;
        PokemonType type = PokemonType.None;
        while (!success) {
            try {
                System.out.print("Move Type: ");
                type = PokemonType.valueOf(in.nextLine().strip());
                System.out.println();
                success = true;
            }
            catch (Exception e) {
                System.out.println("Invalid Input, please provide valid type name");
            }
        }
        return type;
    }

}
