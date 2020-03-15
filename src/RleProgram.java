import java.util.Scanner;

public class RleProgram {
    public static String toHexString(byte[] data) {
        //number 1
        byte[] arr = encodeRle(data);
        return numsToLetters(arr);
    }
    /*The first method takes in the raw data array, sends it to the method that turns it into the RLE array, and then
    * it sends it to a method I made that turns the decimal values into hexadecimals if necessary.*/
    public static int countRuns(byte[] flatData) {
        //number 2
        int count = 0;
        int toBreak = 0;
        for (int i = 0; i < (flatData.length - 1); i++) {
            if (flatData[i] != flatData[i + 1]) {
                count++;
                toBreak = 0;
            } else {
                toBreak++;
                if (toBreak == 15) {
                    count++;
                    toBreak = 0;
                }
            }
        }
        return count + 1;
    }
    /*This method goes through each element of the array, compares it to the next element, and see if they're the same.
    * if so, it will keep counting "toBreak" until it reaches 15 or until there is a different element. I added a one at
    * the end because I subtracted a one in the for loop to fix an outOfBounds error.*/
    public static byte[] encodeRle(byte[] flatData) {
        //number 3
        int same = 1;
        int value;
        int space = 0;
        int i;
        int howLong = countRuns(flatData);
        byte[] arr = new byte[howLong * 2];
        for (i = 0; i < (flatData.length - 1); i++) {
            value = flatData[i];
            if (flatData[i] == flatData[i + 1] && same < 15) {
                same++;
            } else if (flatData[i] != flatData[i + 1] || same == 15) {
                arr[space] = (byte) same;
                arr[space + 1] = (byte) value;
                space = space + 2;
                same = 1;
            }
        }
        /*For this method, I created a new array twice the size of the output from countRuns, went through each element
        * in a similar fashion as the method above, and counted how many of the same element there are. When it reaches
        * the limit or a different element, it puts the number of times in one element of the new array and the actual
        * value in the second; then it increases the index of the new array so it can latter add the new values after
        * the ones it just put. Once again, because of out of bounds errors, I had to put the numbers for the last pair
        * of values without using the loop.*/
        if (i == (flatData.length - 1)) {
            arr[space] = (byte) (same);
            arr[space + 1] = flatData[flatData.length - 1];
        }
        return arr;
    }
    public static int getDecodedLength(byte[] rleData) {
        //number 4
        int total = 0;
        for (int i = 0; i < rleData.length; i = i + 2) {
            int byHowMuch = rleData[i];
            total = byHowMuch + total;
        }
        return total;
    }
    /*This method simply counts how many elements there are in the array.*/
    public static byte[] decodeRle(byte[] rleData) {
        //number 5
        int howLong = getDecodedLength(rleData);
        int x = 0;
        int y = 0;
        byte[] arr = new byte[howLong];
        for (int i = 0; i < rleData.length; i = i + 2) {
            int byHowMuch = rleData[i];
            int num = rleData[i + 1];
            while (x < byHowMuch) {
                arr[y] = (byte) num;
                x++;
                y++;
            }
            x = 0;
        }
        return arr;
    }
    /*This method takes the output from method 4 and makes a new array of that size. Then, it looks at the first value
    * and second value of a pair, and it goes on a loop putting the second value the amount of times the first value is.*/
    public static byte[] stringToData(String dataString) {
        //number 6
        String lowercase = dataString.toLowerCase();
        String[] letterArr = lowercase.split("");
        byte[] numberArr = new byte[letterArr.length];
        int num;
        for (int i = 0; i < letterArr.length; i++) {
            String x = letterArr[i];
            char y = x.charAt(0);
            if ('a' <= y && y <= 'f') {
                num = y - 87;
            } else {
                num = y - 48;
            }
            numberArr[i] = (byte) num;
        }
        return numberArr;
    }
    /*This method first makes all the letters lowercase, splits the string by each element and makes that into an array.
    * Then, it goes through each element and turns them into their numerical values. Finally, it puts each element into
    * a new array of the same size as the string array.*/
    public static String toRleString(byte[] rleData) {
      //number 7
        byte[] arr = encodeRle(rleData);
        String[] colonArr = new String[(arr.length + (arr.length / 2)) - 1];
        int x = 0;
        for (int i = 0; i < arr.length; i = i + 2) {
            colonArr[x] = String.valueOf(arr[i]);
            colonArr[x + 1] = String.valueOf(arr[i + 1]);
            if ((i + 1) != (arr.length - 1)) {
                colonArr[x + 2] = ":";
            }
            x = x + 3;
        }
        /*This method takes the raw data array, sends to get encoded into RLE, adds the colons and changes the values to
        * hexadecimals if necessary. The length of the new array is the same as the encoded array adding spaces for x - 1
        * colons. Then, it puts each element into their respective place in the new array, except every three spaces it
        * adds a colon (except at the end). Below this comment is a switch statement to go through every 3 elements and
        * change them into hexadecimals. It then takes that array and passes it through a method I made to turn it into
        * a string.*/
        for (int i = 1; i < colonArr.length; i = i + 3) {
            switch (colonArr[i]) {
                case "10":
                    colonArr[i] = "a";
                    break;
                case "11":
                    colonArr[i] = "b";
                    break;
                case "12":
                    colonArr[i] = "c";
                    break;
                case "13":
                    colonArr[i] = "d";
                    break;
                case "14":
                    colonArr[i] = "e";
                    break;
                case "15":
                    colonArr[i] = "f";
                    break;
                default:
                    colonArr[i] = colonArr[i];
                    break;
            }
        }
        /*This is the method to turn it into a string. In the switch statement, the number stays the same if it is less
        * than 10.*/
        return arrToStr(colonArr);
    }
    public static byte[] stringToRle(String rleString) {
        //number 8
        int howMany;
        int totalHowMany = 0;
        int componentsIndex = 0;
        int newIndex = 0;
        int howManyTotal = 0;
        String[] newArr;
        String[] subArr;
        String rleLowercase = rleString.toLowerCase();
        String[] components = rleLowercase.split(":");
        while (componentsIndex < components.length) {
            howMany = components[componentsIndex].length();
            if (howMany == 2) {
                howManyTotal = howMany + howManyTotal;
            } else if (howMany == 3) {
                howManyTotal = (howMany - 1) + howManyTotal;
            }
            componentsIndex++;
        }
        /*This method makes the string lowercase and splits it by each colon into an array. Then, depending on if there
        * are three or two characters in one element, it counts how many spaces the new array will need.*/
        newArr = new String[howManyTotal];
        componentsIndex = 0;
        while (componentsIndex < components.length) {
            howMany = components[componentsIndex].length();
            if (howMany == 2) {
                subArr = components[componentsIndex].split("");
                newArr[newIndex] = subArr[0];
                newArr[newIndex + 1] = subArr[1];
                newIndex = newIndex + 2;
            } else if (howMany == 3) {
                subArr = components[componentsIndex].split("");
                String newFirst = subArr[0] + subArr[1];
                newArr[newIndex] = newFirst;
                newArr[newIndex + 1] = subArr[2];
                newIndex = newIndex + 2;
            }
            componentsIndex++;
            totalHowMany = totalHowMany + howMany;
        }
        /*Now, it will do the same process again, except that if there are two characters, it adds them into the new
        * array separately and, if there is three, it puts the first two characters together as one in the new array
        * while the third one stays the same.*/
        byte[] result = new byte[newArr.length];
        for (int x = 0; x < newArr.length; x++) {
            switch (newArr[x]) {
                case "a":
                    result[x] = 10;
                    break;
                case "b":
                    result[x] = 11;
                    break;
                case "c":
                    result[x] = 12;
                    break;
                case "d":
                    result[x] = 13;
                    break;
                case "e":
                    result[x] = 14;
                    break;
                case "f":
                    result[x] = 15;
                    break;
                    /*Here, it looks at each element of the array and either turns it into its decimal value or it
                    * parses the numerical value into byte.*/
                default:
                    result[x] = Byte.parseByte(newArr[x]);
                    break;
            }

        }
        return result;
    }
    public static String arrToStr(String[] decodedRLE) {
        //extra method made by me
        StringBuilder str = new StringBuilder();
        for (String b : decodedRLE) {
            str.append(b);
        }
        return str.toString();
    }
    /*This method takes each element of the string array and appends it together into one string.*/
    public static String numsToLetters(byte[] rawArr) {
        //another extra method by yours truly
        String[] letterArr = new String[rawArr.length];
        for (int i = 0; i < rawArr.length; i++) {
            if (rawArr[i] >= 10 && rawArr[i] < 16) {
                char x = (char) ((char) rawArr[i] + 87);
                String y = Character.toString(x);
                letterArr[i] = y;
            } else {
                letterArr[i] = String.valueOf(rawArr[i]);
            }
        }
        return arrToStr(letterArr);
    }
    /*This method takes each value in a byte array and either turns it into its hexadecimal character or leaves it as
    * it is. Then it passes the string array through the method above this one to turn it into a string.*/
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String menu = "RLE Menu\n--------\n0. Exit\n1. Load File\n2. Load Test Image\n3. Read RLE String\n" +
                "4. Read RLE Hex String\n5. Read Data Hex String\n6. Display Image\n7. Display RLE String\n" +
                "8. Display Hex RLE Data\n9. Display Hex Flat Data";
        String inputString;
        String result;
        byte[] loadedImage = new byte[0];
        byte[] toStore;
        byte[] toDisplay = new byte[0];
        System.out.println("Welcome to the RLE image encoder!");
        System.out.println("Displaying Spectrum Image:");
        byte[] spectrumImage = ConsoleGfx.testRainbow;
        ConsoleGfx.displayImage(spectrumImage);
        /*This part is just declaring all the variables and displaying the menu components.*/
        while (true) {
            System.out.println(menu);
            System.out.print("Select a Menu Option: ");
            int menuOption = input.nextInt();
            if (menuOption == 0) {
                break;
            } else if (menuOption == 1) {
                System.out.print("Enter name of file to load: ");
                String fileName = input.next();
                loadedImage = ConsoleGfx.loadFile(fileName);
            } else if (menuOption == 2) {
                loadedImage = ConsoleGfx.testImage;
                System.out.println("Test image data loaded.");
                /*The loop goes on forever or until the menu option is 0. The first and second options simply call
                * on the methods from ConsoleGfx.java.*/
            } else if (menuOption == 3) {
                System.out.print("Enter an RLE string to be decoded: ");
                inputString = input.next();
                toStore = stringToRle(inputString); //works but not flat yet, done in the last options
                toDisplay = decodeRle(toStore);
                //System.out.println(Arrays.toString(toDisplay));
            } else if (menuOption == 4) {
                System.out.print("Enter the hex string holding RLE data: ");
                inputString = input.next();
                toStore = stringToData(inputString); //works but not flat yet
                toDisplay = decodeRle(toStore);
                //System.out.println(Arrays.toString(toDisplay));
            } else if (menuOption == 5) {
                System.out.print("Enter the hex string holding flat data: ");
                inputString = input.next();
                toDisplay = stringToData(inputString); //works & already flat
                //System.out.println(Arrays.toString(toDisplay));
                /*For options 3 and 4, it passes the input through the respective method that will turn them into RLE
                * byte data arrays and then it passes that through the decode method so that they all have the same form
                * before turning them into anything else.*/
            } else if (menuOption == 6) {
                System.out.println("Displaying image...");
                ConsoleGfx.displayImage(loadedImage);
            } else if (menuOption == 7) {
                result = toRleString(toDisplay);
                System.out.println("RLE representation: " + result);
            } else if (menuOption == 8) {
                result = toHexString(toDisplay);
                System.out.println("RLE hex values: " + result);
            } else if (menuOption == 9) {
                result = numsToLetters(toDisplay);
                System.out.println("Flat hex values: " + result);
            } else {
                System.out.println("ERROR");
            }
            /*Option 6 just displays whatever file loadedImage is. Options 7 and 8 pass the raw data array though their
            * respective methods, and option 8 just takes the raw data, changes the decimal values to hexadecimal if
            * necessary, and turns that into a string. At the end, I also put an error message for other menu inputs;
            * it doesn't seem to affect my score on the project, so I am going to leave it there.*/
        }
    }
}