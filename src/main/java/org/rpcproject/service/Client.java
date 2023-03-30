package org.rpcproject.service;

import edu.emory.mathcs.backport.java.util.Arrays;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Client {
    private String hostUrl;
    private Integer hostPort;
    private ManagedChannel channel;
    private org.rpcproject.grpc.EduCostServiceGrpc.EduCostServiceBlockingStub stub;


    public Client(String hostUrl,Integer hostPort){
        this.hostUrl = hostUrl;
        this.hostPort = hostPort;

        this.channel = ManagedChannelBuilder.forAddress(this.hostUrl, this.hostPort)
                .usePlaintext()
                .build();

        this.stub = org.rpcproject.grpc.EduCostServiceGrpc
                .newBlockingStub(channel);
    }

    public void start() {
        ManagedChannelBuilder.forAddress(this.hostUrl, this.hostPort)
                .usePlaintext()
                .build();
        org.rpcproject.grpc.EduCostServiceGrpc.EduCostServiceBlockingStub stub = org.rpcproject.grpc.EduCostServiceGrpc
                        .newBlockingStub(channel);


        while(true) {
            Integer option = 0;
            System.out.println("Please choose any of the option below : ");
            System.out.println("[1] Get cost");
            System.out.println("[2] Get top 5 most expensive states");
            System.out.println("[3] Get top 5 most economic states");
            System.out.println("[4] Get top 5 highest growth rate");
            System.out.println("[5] Get aggregate region‘s average overall expense");
            Scanner scanner = new Scanner(System.in);
            option = scanner.nextInt();

            switch (option) {
                case 1 :
                    getCost();
                    break;
                case 2:
                    getTopFiveMostExpensiveStates();
                    break;
                case 3:
                    getTopFiveEconomicStates();
                    break;
                case 4:
                    getTopFiveHighestGrowthRate();
                    break;
                case 5:
                    getRegionOverAllRateExpense();
                    break;
            }

            System.out.println("Do you want to continue ? ");
            System.out.println("[1] Yes");
            System.out.println("[2] No");
            Scanner inp = new Scanner(System.in);
            Integer opt = inp.nextInt();

            if (opt == 2) {
                break;
            }

        }
    }
    public void getCost() {
        String expense = "";
        String year = "";
        String state = "";
        String type = "";
        String length = "";

        Scanner input = new Scanner(System.in);
        System.out.println("Enter the year : ");
        year = input.nextLine();

        System.out.println("Enter the state : ");
        state = input.nextLine();

        System.out.println("Enter the expense : ");
        expense = input.nextLine();

        System.out.println("Enter the type : ");
        type = input.nextLine();

        System.out.println("Enter the length : ");
        length = input.nextLine();

        org.rpcproject.grpc.QueryRequest req = org.rpcproject.grpc.QueryRequest
                .newBuilder()
                .setExpense(expense)
                .setYear(year)
                .setLength(length)
                .setState(state)
                .setType(type)
                .build();
        org.rpcproject.grpc.ResponseToQuery res = this.stub.queryCost(req);

        System.out.println("Server response : Cost : "+res.getCost());
    }
    public void getTopFiveMostExpensiveStates() {
        String year = "";
        String type = "";
        String length = "";

        Scanner input = new Scanner(System.in);
        System.out.println("Enter the year : ");
        year = input.nextLine();

        System.out.println("Enter the type : ");
        type = input.nextLine();

        System.out.println("Enter the length : ");
        length = input.nextLine();

        org.rpcproject.grpc.ExpensiveStateQuery req = org.rpcproject.grpc.ExpensiveStateQuery
                .newBuilder()
                .setLength(length)
                .setType(type)
                .setYear(year)
                .build();

        org.rpcproject.grpc.ExpensiveStateResponse res = this.stub.queryExpensiveStateList(req);

        System.out.println("Result expensive stat list is given below ");

        Iterator itter = res.getStatesList().iterator();
        while(itter.hasNext()) {
            System.out.println(itter.next());
        }
    }

    public void getTopFiveEconomicStates() {
        String year = "";
        String type = "";
        String length = "";

        Scanner input = new Scanner(System.in);
        System.out.println("Enter the year : ");
        year = input.nextLine();

        System.out.println("Enter the type : ");
        type = input.nextLine();

        System.out.println("Enter the length : ");
        length = input.nextLine();

        org.rpcproject.grpc.EconomicStateQuery req = org.rpcproject.grpc.EconomicStateQuery
                .newBuilder()
                .setYear(year)
                .setLength(length)
                .setType(type)
                .build();

        org.rpcproject.grpc.EconomicStateResponseList res = this.stub.queryEconomicStateList(req);

        System.out.println("Economic state response list : ");
        Iterator itter = res.getEconomicStatResponseListList().iterator();
        while(itter.hasNext()){
            org.rpcproject.grpc.EconomicStateResponse item = (org.rpcproject.grpc.EconomicStateResponse) itter.next();
            System.out.println("Year: "+item.getYear() + " Expense : "+item.getExpense() + " Length : "+item.getLength()+ " Type : "+item.getType());
        }

    } // FIXME: 3/31/2023

    public void getTopFiveHighestGrowthRate() {
        String oneYear = "";
        String threeYear = "";
        String fiveYear = "";
        String type = "";
        String length = "";

        Scanner input = new Scanner(System.in);
        System.out.println("Enter the fir year : ");
        oneYear = input.nextLine();

        System.out.println("Enter the third year : ");
        threeYear = input.nextLine();

        System.out.println("Enter the fifth year : ");
        fiveYear = input.nextLine();

        System.out.println("Enter the type : ");
        type = input.nextLine();

        System.out.println("Enter the length : ");
        length = input.nextLine();

        List<String> yearList = java.util.Arrays.asList(oneYear,threeYear,fiveYear);


        org.rpcproject.grpc.HighestGrowthRateQuery req = org.rpcproject.grpc.HighestGrowthRateQuery
                .newBuilder()
                .setLength(length)
                .setType(type)
                .addAllYears(yearList)
                .build();

        org.rpcproject.grpc.HighestGrowthRateResponseList res = this.stub.queryHighestGrowthRateList(req);

        System.out.println("Highest growth rate list is given below : ");

        Iterator itter = res.getHighestGrowthRateResponseListList().iterator();

        while(itter.hasNext()) {
            org.rpcproject.grpc.HighestGrowthRateResponse item = (org.rpcproject.grpc.HighestGrowthRateResponse) itter.next();
            System.out.println("State : "+item.getState() + " Expense : "+item.getExpense());
        }

    } // FIXME: 3/31/2023

    public void getRegionOverAllRateExpense() {
        String year = "";
        String type = "";
        String length = "";
        String region = "";

        Scanner input = new Scanner(System.in);
        System.out.println("Enter the year : ");
        year = input.nextLine();

        System.out.println("Enter the type : ");
        type = input.nextLine();

        System.out.println("Enter the length : ");
        length = input.nextLine();

        System.out.println("Enter the Region : ");
        region = input.nextLine();


        org.rpcproject.grpc.AggragateRegionsOverallExpenseQuery req = org.rpcproject.grpc.AggragateRegionsOverallExpenseQuery
                .newBuilder()
                .setRegion(region)
                .setYear(year)
                .setLength(length)
                .setType(type)
                .setType(type)
                .build();

        org.rpcproject.grpc.AggragateRegionsOverallExpenseResponse res = this.stub.queryAggragateRegionsOverallExpense(req);

        System.out.println("Region average overall expense is : "+res.getOverallExpense() + " for Year: "+res.getYear());
    }
}
