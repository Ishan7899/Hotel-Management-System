import javax.sound.midi.spi.SoundbankReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;


public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "ishangadi1235";

    public static void main(String[] args) throws ClassNotFoundException,SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Statement statement = connection.createStatement();

            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Reserve a room.");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.println("Choose an option");

                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        reserveRoom(connection,scanner,statement);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection,scanner);
                        break;
                    case 4:
                        updateReservation(connection,scanner);
                        break;
                    case 5:
                        deleteReservation(connection,scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again");
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }

    }

    private static void reserveRoom(Connection connection,Scanner scanner,Statement statement) throws SQLException{
        try{
            System.out.println("Enter Guest name: ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.println("Enter Room number: ");
            int roomNumber = scanner.nextInt();
            System.out.println("Enter Contact Number: ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO reservations (guest_name,room_number,contact_number)" + "VALUES ('" + guestName + "'," + roomNumber + ",'" + contactNumber + "')";

            try{
                int affectedRows = statement.executeUpdate(sql);
                if(affectedRows > 0){
                    System.out.println("Reservation Successful");
                }
                else{
                    System.out.println("Reservation failed");
                }
            } finally {
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void viewReservations(Connection connection) throws SQLException {
        String sql = "SELECT reservation_id,guest_name,room_number,room_number,contact_number,reservation_date FROM reservations";

        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            while(resultSet.next()){
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("room_number");
                String contact_number = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                System.out.println("Reservation ID is: "+reservationId);
                System.out.println("Room number is: "+guestName);
                System.out.println("Contact Number is: "+contact_number);
                System.out.println("Reservation date is: "+reservationDate);
            }
        }
    }

    private static void getRoomNumber(Connection connection,Scanner scanner){
        try{
            System.out.println("Enter reservation ID");
            int reservationID =scanner.nextInt();
            System.out.println("Enter Guest name");
            String guestName = scanner.next();

            String sql = "SELECT room_number FROM reservations WHERE reservation_id = " + reservationID + " AND guest_name = '" + guestName + "'";

            try(Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){
                if(resultSet.next()){
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for reservation ID: " + reservationID + "and Guests " + guestName + " is: "+ roomNumber);
                }else{
                    System.out.println("Reservation not found for the given ID and guest name");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection connection,Scanner scanner){
        try{
            System.out.println("Enter reservation ID to update: ");
            int reservationID = scanner.nextInt();
            scanner.nextLine();

            if(!reservationExists(connection,reservationID)){
                System.out.println("Reservation not found for the given ID");
                return;
            }
            System.out.println("Enter new guest name: ");
            String newGuestName = scanner.nextLine();
            System.out.println("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();
            System.out.println("Enter new contact number: ");
            String newContactNumber = scanner.next();

            String sql = "UPDATE reservations SET guest_name = '"+ newGuestName+ "' , " + " room_number = " + newRoomNumber + "," + " contact_number = '" + newContactNumber +"' WHERE reservation_id = " + reservationID;

            try(Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(sql);

                if(affectedRows>0){
                    System.out.println("Reservation updated successfully");
                }else{
                    System.out.println("Reservation update failed");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection,Scanner scanner){
        try{
            System.out.println("Enter reservation ID to delete: ");
            int reservationID = scanner.nextInt();

            if(!reservationExists(connection,reservationID)){
                System.out.println("Reservation not found for given ID");
                return;
            }
            String sql = "DELETE FROM reservations WHERE  reservation_id = " + reservationID;

            try(Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(sql);

                if(affectedRows>0){
                    System.out.println("Reservation deleted successfully!");
                }else{
                    System.out.println("Reservation deletion failed");
                }

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    private static boolean reservationExists(Connection connection,int reservationID){
        try{
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id= " + reservationID;

            try(Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){
                return resultSet.next(); //If there is a result,the reservation exists
            }
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static void exit() throws InterruptedException{
        System.out.print("Exiting system");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using Hotel Reservation System");
    }
}