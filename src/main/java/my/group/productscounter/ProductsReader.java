package my.group.productscounter;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

class ProductsReader {
    public static void main(String[] args) throws IOException {
        String filePath = "products.csv";

        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // Skip header
        bufferedReader.readLine();

        String line;
        String name;
        String material;
        String d1;
        String d2;
        String d3;
        String join;
        while((line = bufferedReader.readLine()) != null){
            String[] values = line.split(";");

            name = values[1].trim();
            material = values[2].trim();
            d1 = values[3].trim();
            d2 = values[4].trim();
            d3 = values[5].trim();
            join = values[6].trim();

            System.out.println(name);
            System.out.println(material);
            System.out.println(d1);
            System.out.println(d2);
            System.out.println(d3);
            System.out.println(join);
        }
    }
}
