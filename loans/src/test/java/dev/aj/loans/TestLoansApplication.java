package dev.aj.loans;

import org.springframework.boot.SpringApplication;

public class TestLoansApplication {

   public static void main(String[] args) {
       SpringApplication.from(LoansApplication::main)
               .withAdditionalProfiles( "test")
               .run(args);
    }

}
