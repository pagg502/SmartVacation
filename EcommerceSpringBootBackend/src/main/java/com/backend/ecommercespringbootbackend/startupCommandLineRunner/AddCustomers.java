package com.backend.ecommercespringbootbackend.startupCommandLineRunner;

import com.backend.ecommercespringbootbackend.dao.CustomerRepository;
import com.backend.ecommercespringbootbackend.dao.DivisionRepository;
import com.backend.ecommercespringbootbackend.entities.Customer;
import com.backend.ecommercespringbootbackend.entities.Division;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AddCustomers implements CommandLineRunner {
    @Autowired
    CustomerRepository customerRepository;
    public boolean doesCustomerExist(String firstName) {
        return customerRepository.existsByFirstNameIgnoreCase(firstName);
    }

    public boolean doesCustomerExistbyEmail(String email) {
        return customerRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public void run(String... args) throws Exception {
        Customer customer;
        DivisionRepository divisionRepository;
        Division division = new Division();

        if (!doesCustomerExist("Yun")){
            //Set division ID to 2
            division.setId(2L);
            customer = new Customer("Yun", "K","yun@gmail.com", "$2a$10$ED5otquW1YdTP0Q2buLiEuJd1rsW3WQKveHRFtUr4W2pZrVlpStFK", "108 Bank St", "47809", "123-999-9009", division);
            customerRepository.save(customer);
            System.out.println("Customer Yun successfully added.");
        }
        if (!doesCustomerExist("Bob")){
            //Set division ID to 3
            division.setId(3L);
            customer = new Customer("Bob", "T","bob@gmail.com", "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u", "1708 3rd St", "40809", "999-999-9009", division);
            customerRepository.save(customer);
            System.out.println("Customer Bob successfully added.");
        }
        if (!doesCustomerExist("Jen")){
            //Set division ID to 4
            division.setId(4L);
            customer = new Customer("Jen", "R","jen@gmail.com", "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u", "2308 4th St", "40179", "432-999-4509", division);
            customerRepository.save(customer);
            System.out.println("Customer Jen successfully added.");
        }
        if (!doesCustomerExist("Tom")){
            //Set division ID to 5
            division.setId(5L);
            customer = new Customer("Tom", "S","tom@gmail.com", "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u", "6785 5th St", "20987", "409-911-4536", division);
            customerRepository.save(customer);
            System.out.println("Customer Tom successfully added.");
        }
        if (!doesCustomerExist("Peter")){
            //Set division ID to 6
            division.setId(6L);
            customer = new Customer("Peter", "G","peterpan@gmail.com", "$$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u", "98709 Programmer St", "12809", "243-711-4436", division);
            customerRepository.save(customer);
            System.out.println("Customer Peter successfully added.");
        }
        if (!doesCustomerExist("Alice")){
            //Set division ID to 7
            division.setId(7L);
            customer = new Customer(
                    "Alice", "M", "alice@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "120 Maple Ave", "33445", "321-555-7890", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Alice successfully added.");
        }
        if (!doesCustomerExist("Brian")){
            //Set division ID to 8
            division.setId(8L);
            customer = new Customer(
                    "Brian", "K", "brian@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "88 Ocean Blvd", "77821", "713-444-3321", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Brian successfully added.");
        }
        if (!doesCustomerExist("Catherine")){
            //Set division ID to 9
            division.setId(9L);
            customer = new Customer(
                    "Catherine", "L", "catherine@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "451 Pine St", "90210", "818-333-9090", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Catherine successfully added.");
        }
        if (!doesCustomerExist("Daniel")){
            //Set division ID to 10
            division.setId(10L);
            customer = new Customer(
                    "Daniel", "R", "daniel@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "77 Sunset Way", "44112", "216-555-8181", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Daniel successfully added.");
        }
        if (!doesCustomerExist("Emily")){
            //Set division ID to 11
            division.setId(11L);
            customer = new Customer(
                    "Emily", "T", "emily@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "999 Willow Rd", "55001", "612-777-1212", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Emily successfully added.");
        }
        if (!doesCustomerExist("Frank")){
            //Set division ID to 12
            division.setId(12L);
            customer = new Customer(
                    "Frank", "D", "frank@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "65 Industrial Pkwy", "60616", "312-888-3434", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Frank successfully added.");
        }
        if (!doesCustomerExist("Grace")){
            //Set division ID to 13
            division.setId(13L);
            customer = new Customer(
                    "Grace", "H", "grace@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "302 River St", "10012", "917-222-5656", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Grace successfully added.");
        }
        if (!doesCustomerExist("Henry")){
            //Set division ID to 14
            division.setId(14L);
            customer = new Customer(
                    "Henry", "B", "henry@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "18 Lakeview Dr", "48301", "248-666-9090", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Henry successfully added.");
        }
        if (!doesCustomerExist("Isabella")){
            //Set division ID to 15
            division.setId(15L);
            customer = new Customer(
                    "Isabella", "N", "isabella@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "742 Garden Ct", "33139", "305-444-7878", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Isabella successfully added.");
        }
        if (!doesCustomerExist("Jack")){
            //Set division ID to 16
            division.setId(16L);
            customer = new Customer(
                    "Jack", "W", "jack@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "500 Tech Park", "95054", "408-999-1212", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Jack successfully added.");
        }
        if (!doesCustomerExist("Karen")){
            //Set division ID to 17
            division.setId(17L);
            customer = new Customer(
                    "Karen", "P", "karen@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "234 Birch Ln", "46204", "317-555-1414", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Karen successfully added.");
        }
        if (!doesCustomerExist("Leo")){
            //Set division ID to 18
            division.setId(18L);
            customer = new Customer(
                    "Leo", "S", "leo@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "901 Market St", "94103", "415-333-7878", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Leo successfully added.");
        }
        if (!doesCustomerExist("Megan")){
            //Set division ID to 19
            division.setId(19L);
            customer = new Customer(
                    "Megan", "A", "megan@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "77 Rosewood Dr", "30022", "770-888-5656", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Megan successfully added.");
        }
        if (!doesCustomerExist("Nathan")){
            //Set division ID to 20
            division.setId(20L);
            customer = new Customer(
                    "Nathan", "E", "nathan@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "640 College Ave", "80302", "303-777-2929", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Nathan successfully added.");
        }
        if (!doesCustomerExist("Olivia")){
            //Set division ID to 21
            division.setId(21L);
            customer = new Customer(
                    "Olivia", "C", "olivia@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "155 Bay St", "02110", "617-444-9090", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Olivia successfully added.");
        }
        if (!doesCustomerExist("Paul")){
            //Set division ID to 22
            division.setId(22L);
            customer = new Customer(
                    "Paul", "J", "paul@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "82 Harbor Rd", "02840", "401-666-3434", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Paul successfully added.");
        }
        if (!doesCustomerExist("Quinn")){
            //Set division ID to 23
            division.setId(23L);
            customer = new Customer(
                    "Quinn", "F", "quinn@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "19 Cypress Ct", "70118", "504-555-7878", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Quinn successfully added.");
        }
        if (!doesCustomerExist("Rachel")){
            //Set division ID to 24
            division.setId(24L);
            customer = new Customer(
                    "Rachel", "V", "rachel@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "400 Elm St", "15213", "412-999-2323", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Rachel successfully added.");
        }

        if (!doesCustomerExist("Samuel")){
            //Set division ID to 25
            division.setId(25L);
            customer = new Customer(
                    "Samuel", "O", "samuel@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "73 Mountain Rd", "05401", "802-333-5656", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Samuel successfully added.");
        }

        if (!doesCustomerExist("Tina")){
            //Set division ID to 26
            division.setId(26L);
            customer = new Customer(
                    "Tina", "Q", "tina@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "910 Sunset Blvd", "90028", "323-555-1212", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Tina successfully added.");
        }

        if (!doesCustomerExistbyEmail("john.a@gmail.com")){
            //Set division ID to 27
            division.setId(27L);
            customer = new Customer(
                    "John", "A", "john.a@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "101 Canyon Dr", "84003", "801-555-1001", division
            );
            customerRepository.save(customer);
            System.out.println("Customer John successfully added.");
        }

        if (!doesCustomerExistbyEmail("john.b@gmail.com")){
            //Set division ID to 28
            division.setId(28L);
            customer = new Customer(
                    "John", "B", "john.b@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "202 Ridge Ave", "84004", "801-555-1002", division
            );
            customerRepository.save(customer);
            System.out.println("Customer John successfully added.");
        }

        if (!doesCustomerExistbyEmail("john.c@gmail.com")){
            //Set division ID to 29
            division.setId(29L);
            customer = new Customer(
                    "John", "C", "john.c@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "303 Valley Rd", "84005", "801-555-1003", division
            );
            customerRepository.save(customer);
            System.out.println("Customer John successfully added.");
        }

        if (!doesCustomerExistbyEmail("john.d@gmail.com")){
            //Set division ID to 30
            division.setId(30L);
            customer = new Customer(
                    "John", "D", "john.d@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "404 Aspen St", "84006", "801-555-1004", division
            );
            customerRepository.save(customer);
            System.out.println("Customer John successfully added.");
        }

        if (!doesCustomerExistbyEmail("john.e@gmail.com")){
            //Set division ID to 31
            division.setId(31L);
            customer = new Customer(
                    "John", "E", "john.e@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "505 Pine Ln", "84007", "801-555-1005", division
            );
            customerRepository.save(customer);
            System.out.println("Customer John successfully added.");
        }

        if (!doesCustomerExistbyEmail("mike.r@gmail.com")){
            //Set division ID to 32
            division.setId(32L);
            customer = new Customer(
                    "Mike", "R", "mike.r@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "12 Broadway", "10001", "212-555-2001", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Mike successfully added.");
        }

        if (!doesCustomerExistbyEmail("mike.s@gmail.com")){
            //Set division ID to 33
            division.setId(33L);
            customer = new Customer(
                    "Mike", "S", "mike.s@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "34 Madison Ave", "10002", "212-555-2002", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Mike successfully added.");
        }

        if (!doesCustomerExistbyEmail("anna.l@gmail.com")){
            //Set division ID to 34
            division.setId(34L);
            customer = new Customer(
                    "Anna", "L", "anna.l@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "77 Market St", "94103", "415-555-3001", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Anna successfully added.");
        }

        if (!doesCustomerExistbyEmail("annam.m@gmail.com")){
            //Set division ID to 35
            division.setId(35L);
            customer = new Customer(
                    "Anna", "M", "annam.m@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "88 Castro St", "94110", "415-555-3002", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Anna successfully added.");
        }

        if (!doesCustomerExistbyEmail("john.a1@gmail.com")){
            //Set division ID to 45
            division.setId(45L);
            customer = new Customer(
                    "John", "A1", "john.a1@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "111 Canyon View Rd", "84013", "801-555-4001", division
            );
            customerRepository.save(customer);
            System.out.println("Customer John successfully added.");
        }

        if (!doesCustomerExistbyEmail("john.b1@gmail.com")){
            //Set division ID to 46
            division.setId(46L);
            customer = new Customer(
                    "John", "B1", "john.b1@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "122 Canyon View Rd", "84014", "801-555-4002", division
            );
            customerRepository.save(customer);
            System.out.println("Customer John successfully added.");
        }

        if (!doesCustomerExistbyEmail("mike.r1@gmail.com")){
            //Set division ID to 47
            division.setId(47L);
            customer = new Customer(
                    "Mike", "R1", "mike.r1@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "200 Broadway", "10003", "212-555-5001", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Mike successfully added.");
        }

        if (!doesCustomerExistbyEmail("mike.s1@gmail.com")){
            //Set division ID to 48
            division.setId(48L);
            customer = new Customer(
                    "Mike", "S1", "mike.s1@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "210 Broadway", "10004", "212-555-5002", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Mike successfully added.");
        }

        if (!doesCustomerExistbyEmail("sarah.k1@gmail.com")){
            //Set division ID to 49
            division.setId(49L);
            customer = new Customer(
                    "Sarah", "K1", "sarah.k1@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "45 Lake Shore Dr", "84015", "801-555-4003", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Sarah successfully added.");
        }

        if (!doesCustomerExistbyEmail("sarah.m1@gmail.com")){
            //Set division ID to 10
            division.setId(10L);
            customer = new Customer(
                    "Sarah", "M1", "sarah.m1@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "55 Lake Shore Dr", "84016", "801-555-4004", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Sarah successfully added.");
        }

        if (!doesCustomerExistbyEmail("john.c1@gmail.com")){
            //Set division ID to 31
            division.setId(31L);
            customer = new Customer(
                    "John", "C1", "john.c1@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "133 Ridgecrest Ln", "84017", "801-555-4005", division
            );
            customerRepository.save(customer);
            System.out.println("Customer John successfully added.");
        }

        if (!doesCustomerExistbyEmail("emily.p1@gmail.com")){
            //Set division ID to 52
            division.setId(52L);
            customer = new Customer(
                    "Emily", "P1", "emily.p1@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "500 Maple St", "60603", "312-555-6001", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Emily successfully added.");
        }

        if (!doesCustomerExistbyEmail("emily.r1@gmail.com")){
            //Set division ID to 54
            division.setId(54L);
            customer = new Customer(
                    "Emily", "R1", "emily.r1@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "510 Maple St", "60604", "312-555-6002", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Emily successfully added.");
        }
        if (!doesCustomerExistbyEmail("test@gmail.com")){
            //Set division ID to 54
            division.setId(54L);
            customer = new Customer(
                    "Test", "TestR1", "test@gmail.com",
                    "$2a$10$LzKUXUxPzixdK1arsXBv0.nYBs4oBDP9g8cEVlAIyLnkyi3ldj0.u",
                    "510 Maple St", "60604", "312-555-6002", division
            );
            customerRepository.save(customer);
            System.out.println("Customer Test successfully added.");
        }
    }
}
