import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

public class lab6 {
    static class Element{
        int data;
        int line;
        public Element(int tag, int line_num){
            data = tag;
            line = line_num;
        }
    }
    static final int HEX = 16;
    static int total_number = 0;
    static int tag1_hit = 0;
    static int tag2_hit = 0;
    static int tag3_hit = 0;
    static int tag4_hit = 0;
    static int tag5_hit = 0;
    static int tag6_hit = 0;
    static int tag7_hit = 0;

    static final int CACHE_ONE_LINE = 512; //Math.pow(2, 9)
    static final int CACHE_TWO_LINE = 256; //Math.pow(2, 8)
    static final int CACHE_THREE_LINE = 128; //2^7
    static final int CACHE_FOUR_LINE = 512;
    static final int CACHE_FIVE_LINE = 512;
    static final int CACHE_SIX_LINE = 128;
    static final int CACHE_SEVEN_LINE = 1024;
    static int tag1[] = new int [CACHE_ONE_LINE];
    static int tag2[] = new int [CACHE_TWO_LINE];
    static int tag3[] = new int [CACHE_THREE_LINE];
    static int tag7[] = new int [CACHE_SEVEN_LINE];
    static Element tag4_first[] = new Element[CACHE_FOUR_LINE / 2];
    static Element tag4_second[] = new Element [CACHE_FOUR_LINE / 2];
    static Element tag5_first[] = new Element[CACHE_FIVE_LINE / 4 ];
    static Element tag5_second[] = new Element[CACHE_FIVE_LINE / 4];
    static Element tag5_third[] = new Element[CACHE_FIVE_LINE / 4];
    static Element tag5_last[] = new Element[CACHE_FIVE_LINE / 4];
    static Element tag6_first[] = new Element[CACHE_SIX_LINE / 4];
    static Element tag6_second[] = new Element[CACHE_SIX_LINE / 4];
    static Element tag6_third [] = new Element[CACHE_SIX_LINE / 4];
    static Element tag6_last[] = new Element[CACHE_SIX_LINE / 4];

    public static void main(String[] args) throws FileNotFoundException {
//        if(args.length != 1){
//            System.out.println("Usage: java lab6 file");
//            return;
//        }
        process_file(args);
        print_output();
    }
    public static void print_output (){
        System.out.println("Cache #1");
        System.out.println("Cache size: 2048B\t\tAssociativity: 1\tBlock size: 1");
        System.out.printf("Hits: %d   Hit Rate: %.2f%%\n", tag1_hit, get_rate(tag1_hit));
        System.out.println("Cache #2");
        System.out.println("Cache size: 2048B\t\tAssociativity: 1\tBlock size: 2");
        System.out.printf("Hits: %d   Hit Rate: %.2f%%\n", tag2_hit, get_rate(tag2_hit));
        System.out.println("Cache #3");
        System.out.println("Cache size: 2048B\t\tAssociativity: 1\tBlock size: 4");
        System.out.printf("Hits: %d   Hit Rate: %.2f%%\n", tag3_hit, get_rate(tag3_hit));
        System.out.println("Cache #4");
        System.out.println("Cache size: 2048B\t\tAssociativity: 2\tBlock size: 1");
        System.out.printf("Hits: %d   Hit Rate: %.2f%%\n", tag4_hit, get_rate(tag4_hit));
        System.out.println("Cache #5");
        System.out.println("Cache size: 2048B\t\tAssociativity: 4\tBlock size: 1");
        System.out.printf("Hits: %d   Hit Rate: %.2f%%\n", tag5_hit, get_rate(tag5_hit));
        System.out.println("Cache #6");
        System.out.println("Cache size: 2048B\t\tAssociativity: 4\tBlock size: 4");
        System.out.printf("Hits: %d   Hit Rate: %.2f%%\n", tag6_hit, get_rate(tag6_hit));
        System.out.println("Cache #7");
        System.out.println("Cache size: 4096B\t\tAssociativity: 1\tBlock size: 1");
        System.out.printf("Hits: %d   Hit Rate: %.2f%%\n", tag7_hit, get_rate(tag7_hit));
    }
    public static void process_file(String [] args) throws FileNotFoundException {
        //File file = new File(args[0]);
        File file = new File("mem_stream.2");
        Scanner sb = new Scanner(file);
        int address;
        int line = 0;
        String next_line = "";
        while(sb.hasNextLine()){
            next_line = sb.nextLine();
            String [] tmp = next_line.split("\t" , 2);
            address = Integer.parseInt(tmp[1], HEX);
            total_number++;
            process_cache_one(address);
            process_cache_two(address);
            process_cache_three(address);
            process_cache_four(address, line);
            process_cache_five(address, line);
            process_cache_six(address, line);
            process_cache_severn(address);
            line++;
        }
    }
    public static void process_cache_severn(int address){
        /*0 bit block offset, 2 bit byte offset, 10 bit index, 20 bit tag */
        int index = bit_extraction(address, 10, 2);
        int tag = bit_extraction(address, 20, 12);
        if(tag7[index] == tag){
            tag7_hit++;
        }else{
            tag7[index] = tag;
        }
    }
    public static void process_cache_six(int address, int line){
        /*2 bit block offset. 2 bit byte offset, 5 bit index, 23 bit tag */
        int index = bit_extraction(address, 5, 4);
        int tag = bit_extraction(address, 23, 9);
        Element element = new Element(tag, line);
        if(tag6_first[index] == null){
            tag6_first[index] = element;
        }else if(tag6_first[index].data == tag){
            tag6_hit++;
            tag6_first[index].line = line;
        }else if(tag6_second[index] == null){
            tag6_second[index] = element;
        }else if(tag6_second[index].data == tag){
            tag6_hit++;
            tag6_second[index].line = line;
        }else if(tag6_third[index] == null){
            tag6_third[index] = element;
        }else if(tag6_third[index].data == tag){
            tag6_hit++;
            tag6_third[index].line = line;
        }else if(tag6_last[index] == null){
            tag6_last [index] = element;
        }else if(tag6_last[index].data == tag){
            tag6_hit++;
            tag6_last[index].line = line;
        }else{
            int LRU_visited = Integer.MAX_VALUE;
            int flag = 0;
            if(tag6_first[index].line < LRU_visited){
                LRU_visited = tag6_first[index].line;
                flag = 1;
            }
            if(tag6_second[index].line < LRU_visited){
                LRU_visited = tag6_second[index].line;
                flag = 2;
            }
            if (tag6_third[index].line < LRU_visited){
                LRU_visited = tag6_third[index].line;
                flag = 3;
            }
            if (tag6_last [index].line < LRU_visited ){
                flag = 4;
            }
            if (flag == 1){
                tag6_first[index] = element;
            }else if(flag == 2){
                tag6_second [index] = element;
            }else if(flag == 3 ){
                tag6_third [index] = element;
            }else{
                tag6_last[index] = element;
            }
        }
    }
    public static void process_cache_five(int address, int line){
        /*0 bit block offset, 2 bit byte offset, 7 bit index, 23 bit tag */
        int index = bit_extraction(address, 7, 2);
        int tag = bit_extraction(address, 23, 9);
        Element element = new Element(tag, line);
        if(tag5_first[index] == null){
            tag5_first[index] = element;
        }else if(tag5_first[index].data == tag){
            tag5_hit++;
            tag5_first[index].line = line;
        }else if(tag5_second[index] == null){
            tag5_second[index] = element;
        }else if(tag5_second[index].data == tag){
            tag5_hit++;
            tag5_second[index].line = line;
        }else if(tag5_third[index] == null){
            tag5_third [index] = element;
        }else if(tag5_third[index].data == tag){
            tag5_hit++;
            tag5_third[index].line = line;
        }else if(tag5_last[index] == null){
            tag5_last [index] = element;
        }else if(tag5_last[index].data == tag){
            tag5_hit++;
            tag5_last[index].line = line;
        }else{
            int LRU_visited = Integer.MAX_VALUE;
            int flag = 0;
            if (tag5_first[index].line < LRU_visited){
                LRU_visited = tag5_first[index].line;
                flag = 1;
            }
            if(tag5_second[index].line < LRU_visited){
                LRU_visited = tag5_second[index].line;
                flag = 2;
            }
            if(tag5_third[index].line < LRU_visited){
                LRU_visited = tag5_third[index].line;
                flag = 3;
            }
            if (tag5_last[index].line < LRU_visited){
                flag = 4;
            }
            if (flag == 1){
                tag5_first[index] = element;
            }else if(flag == 2){
                tag5_second[index] = element;
            }else if (flag == 3){
                tag5_third[index] =element;
            }else{
                tag5_last[index] = element;
            }
        }
    }
    public static void process_cache_four(int address, int line){
        /* 0 bit block byte, 2 bit offset, 8 bit index, 22 bit tag*/
        int index = bit_extraction(address, 8, 2);
        int tag = bit_extraction(address, 22, 10);
        Element element = new Element(tag, line);
        if(tag4_first[index] == null && tag4_second[index] == null){
            tag4_first[index] = element;
        }else if(tag4_first[index].data == tag){
            tag4_hit++;
            tag4_first[index].line = line;
        }else if(tag4_second[index] == null){
            tag4_second[index] = element;
        }else if(tag4_second[index].data == tag){
            tag4_hit++;
            tag4_second[index].line = line;
        }else{
            /*Neither non-null slots are a hit -> so it is a miss */
            if(tag4_first[index].line < tag4_second[index].line){
                tag4_first[index] = element;
            }else{
                tag4_second[index] = element;
            }
        }
    }
    public static void process_cache_three(int address){
        /* 2 bit byte offset, 2 bit block offset, 7 bit index,
            21 bit tag */
        int index = bit_extraction(address, 7, 4);
        int tag = bit_extraction(address, 21, 11);
        if(tag3[index] == tag){
            tag3_hit++;
        }else{
            tag3[index] = tag;
        }
    }
    public static void process_cache_two(int address){
        /* For cache two, 2 bit byte offset, 1 bit block offset
           8 bit index, 21 bit tag*/
        int index = bit_extraction(address, 8, 3);
        int tag  = bit_extraction(address, 21, 11);
        if(tag2[index] == tag){
            tag2_hit++;
        }else{
            tag2[index] = tag;
        }
    }
    public static void process_cache_one( int address ){
        /*For cache one, 2 bit byte offset, 9 bit index, 21 bit tag */
        int index = bit_extraction(address, 9, 2);
        int tag = bit_extraction(address, 21, 11);
        if (tag1[index] == tag){
            /* That is a hit */
            tag1_hit++;
        }else{
            /*Either no value or It is a miss*/
            tag1[index] = tag;
        }
    }
    public static int bit_extraction(int num, int k, int p){
        return (((1<<k) - 1) & (num >>> p));
    }
    public static double get_rate(int num){
        double ans = ((double)num / total_number) * 100;
        return ans;
    }
}
