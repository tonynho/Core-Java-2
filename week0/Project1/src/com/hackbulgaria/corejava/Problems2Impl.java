package com.hackbulgaria.corejava;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class Problems2Impl implements Problems2 {

    @Override
    public boolean isOdd(int number) {
        return Math.abs(number) % 2 == 1;
    }

    @Override
    public boolean isPrime(int number) {
        if (number == 2) {
            return true;
        } else if ((number < 2) || (number % 2 == 0)) {
            return false;
        }
        for (int i = 3; i * i <= number; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int min(int... array) {
        int min = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    @Override
    public int kthMin(int k, int[] array) {
        if (k > array.length) {
            return 0;
        }

        for (int i = 1; i < array.length - 1; ++i) {
            for (int j = 0; j < i + 1; ++j) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }

        return array[k - 1];
    }

    @Override
    public float getAverage(int[] array) {
        if (array.length == 0) {
            return 0.0f;
        }

        float sum = 0;
        for (int i = 0; i < array.length; ++i) {
            sum += array[i];
        }
        return sum / array.length;
    }

    @Override
    public long getSmallestMultiple(int upperBound) {
        HashMap<Integer, Integer> factors = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> tempFactors;
        ArrayList<Integer> primes = new ArrayList<Integer>();
        int temp = 0, currPrime = 0;
        long result = 1;

        // generate primes
        for (int i = 1; i <= upperBound; ++i) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }

        while (upperBound > 1) {

            // factorize current upperBound
            temp = upperBound;
            currPrime = 0;
            tempFactors = new HashMap<Integer, Integer>();
            while (temp > 1) {
                if (temp % primes.get(currPrime) == 0) {
                    if (tempFactors.containsKey(primes.get(currPrime))) {
                        tempFactors.put(primes.get(currPrime), 1 + tempFactors.get(primes.get(currPrime)));
                    } else {
                        tempFactors.put(primes.get(currPrime), 1);
                    }

                    temp /= primes.get(currPrime);
                } else {
                    ++currPrime;
                }
            }

            // merge factors with tempFactors
            for (int key : tempFactors.keySet()) {
                if (factors.containsKey(key)) {
                    if (factors.get(key) < tempFactors.get(key)) {
                        factors.put(key, tempFactors.get(key));
                    }
                } else {
                    factors.put(key, tempFactors.get(key));
                }
            }

            --upperBound;
        }

        // accumulate result
        for (int key : factors.keySet()) {
            result *= pow(key, factors.get(key));
        }

        return result;
    }
    
    // better solution for getSmallestMultiple
    @Override
    public long getSmallestMultipleBetter(int upperBound) {
        int[] numbers = new int[upperBound];
        for (int i = 0; i < upperBound; ++i) {
            numbers[i] = i + 1;
        }

        int pivot = 1, tempFactor = 0;
        long result = 1;

        while (pivot < upperBound - 1) {
            while (numbers[pivot] == 1) {
                ++pivot;
            }

            tempFactor = numbers[pivot];
            result *= tempFactor;

            for (int i = pivot; i < upperBound; i += pivot + 1) {
                numbers[i] /= tempFactor;
            }

            ++pivot;
        }

        return result;
    }
    
    @Override
    public long getLargestPalindrome(long N) {
        while (N > 0) {
            String str = String.valueOf(N);
            if (str.equals(new StringBuilder(str).reverse().toString())) {
                return N;
            }
            --N;
        }
        return 0;
    }

    @Override
    public int[] histogram(short[][] image) {

        int[] buckets = new int[256];

        for (int row = 0; row < image.length; ++row) {
            for (int col = 0; col < image[row].length; ++col) {
                buckets[image[row][col]] += 1;
            }
        }

        return buckets;
    }

    // helper method for doubleFac
    public static long factorial(int n) {
        long result = 1;
        for (int i = 1; i <= n; ++i) {
            result *= i;
        }
        return result;
    }
    
    @Override
    public long doubleFac(int n) {
        return factorial((int) (factorial(n)));
    }

    @Override
    public long kthFac(int k, int n) {
        long result = 1;
        for (int i = n; i > 0; i -= k) {
            result *= i;
        }
        return result;
    }

    @Override
    public int getOddOccurrence(int[] array) {
        Arrays.sort(array);
        int count = 1, result = 0;
        for (int i = 0; i < array.length - 1; ++i) {
            if (array[i] == array[i + 1]) {
                ++count;
            } else {
                if (count % 2 == 1) {
                    result = array[i];
                    break;
                }
                count = 1;
            }
        }
        return result;
    }

    @Override
    public long pow(int a, int b) {
        if (b == 0) {
            return 1;
        } else if (b % 2 == 0) {
            return pow(a, b / 2) * pow(a, b / 2);
        }
        return a * pow(a, b - 1);
    }

    @Override
    public long maximalScalarSum(int[] a, int[] b) {
        Arrays.sort(a);
        Arrays.sort(b);
        long result = 0;
        
        for (int i = 0; i < a.length; ++i) {
            result += a[i] * b[i];
        }
        return result;
    }

    @Override
    public int maxSpan(int[] array) {
        int maxLen = 0, len = 0;
        for (int left = 0; left < array.length; ++left) {
            for (int right = array.length - 1; right > left; --right) {
                if (array[left] == array[right]) {
                    len = right - left + 1;

                    if (len > maxLen) {
                        maxLen = len;
                    } else if (len < maxLen) {
                        return maxLen;
                    }
                }
            }
        }
        return maxLen;
    }

    @Override
    public boolean canBalance(int[] array) {
        if (array.length == 1) {
            return false;
        }
        
        int left = 0, right = array.length - 1;
        int leftSum = array[left], rightSum = array[right];
        while (left < right) {
            if (leftSum == rightSum) {
                if (right - left == 2) {
                    return false;
                }
                
                ++left;
                leftSum += array[left];
                --right;
                rightSum += array[right];
            } else if (leftSum < rightSum) {
                ++left;
                leftSum += array[left];
            } else {
                --right;
                rightSum += array[right];
            }
        }

        return true;
    }
    
    // helper method for rescale: [xLeft, yTop] and [xRight, yBottom] are inclusive
    @Override
    public int averageMatrix(int[][] matrix, int xLeft, int yTop, int xRight, int yBottom) {
        int sum = 0;

        for (int row = xLeft; row <= yBottom; ++row) {
            for (int col = yTop; col <= xRight; ++col) {
                sum += matrix[row][col];
            }
        }
        
        return sum / ((yBottom - xLeft + 1) * (xRight - yTop + 1));
    }
    
    @Override
    public int[][] rescale(int[][] original, int newWidth, int newHeight) {
        int height = original.length;
        int width = original[0].length;
        
        float hRatio = height / (float) (newHeight);
        float wRatio = width / (float) (newWidth);
        
        int[][] newImage = new int[newHeight][newWidth];
        
        if (hRatio > 1 && wRatio > 1) {     // scale down
            for (int row = 0; row < newHeight; ++row) {
                for (int col = 0; col < newWidth; ++col) {
                    newImage[row][col] = averageMatrix(original, 
                        row * (int) (hRatio), col * (int) (wRatio),
                        (col + 1) * (int) (wRatio) - 1, (row + 1) * (int) (hRatio) - 1);
                }
            }
        }
        
        return newImage;
    }

    @Override
    public String reverseMe(String argument) {
        char[] chars = argument.toCharArray();
        char temp;

        for (int i = 0; i < chars.length / 2; ++i) {
            temp = chars[i];
            chars[i] = chars[chars.length - i - 1];
            chars[chars.length - i - 1] = temp;
        }

        return String.valueOf(chars);
    }

    @Override
    public String copyEveryChar(String input, int k) {
        char[] chars = new char[input.length() * k];
        
        for (int i = 0; i < input.length(); ++i) {
            for (int j = 0; j < k; ++j) {
                chars[i*k + j] = input.charAt(i);
            }
        }

        return String.valueOf(chars);
    }

    @Override
    public String reverseEveryWord(String arg) {
        String[] chunks = arg.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < chunks.length; ++i) {
            chunks[i] = reverseMe(chunks[i]);
            result.append(chunks[i]).append(" ");
        }

        return result.toString().trim();
    }

    @Override
    public boolean isPalindrome(String argument) {
        return argument.equals(new StringBuilder(argument).reverse().toString());
    }

    @Override
    public boolean isPalindrome(int number) {
        return String.valueOf(number).equals(new StringBuilder(String.valueOf(number)).reverse().toString());
    }

    @Override
    public int getPalindromeLength(String input) {
        int count = 0;

        for (int i = input.length() / 2; i > 0; --i) {
            if (input.charAt(i - 1) == input.charAt(input.length() - i)) {
                ++count;
            } else {
                break;
            }
        }

        return count;
    }

    @Override
    public int countOcurrences(String needle, String haystack) {
        return (haystack.length() - haystack.replaceAll(needle, "").length())/needle.length();
    }

    @Override
    public String decodeURL(String input) {
        return input.replaceAll("%20", " ").replaceAll("%3A", ":")
                    .replaceAll("%3D", "?").replaceAll("%2F", "/");
    }

    @Override
    public int sumOfNumbers(String input) {
        String[] numbers = input.replaceAll("[^-\\d]", " ").replaceAll("-{2,}", "").split("\\s+");
        int sum = 0;
        
        for (String s : numbers) {
            if (!s.isEmpty()) {
                sum += Integer.valueOf(s);
            }
        }
        
        return sum;
    }

    @Override
    public boolean areAnagrams(String A, String B) {
        char[] a = A.toCharArray(), b = B.toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        return String.valueOf(a).equals(String.valueOf(b));
    }

    @Override
    public boolean hasAnagramOf(String string, String string2) {
        for (int i = 0; i < string2.length() - string.length() + 1; ++i) {
            if (areAnagrams(string, string2.substring(i, i + string.length()))) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void convertToGreyScale(String imgPath) throws IOException {
        String regex = "^.*\\.(jpg|JPG|png|PNG|bmp|BMP)$";
        if (!Pattern.compile(regex).matcher(imgPath).matches()) {
            return;
        }
        
        BufferedImage image = ImageIO.read(Files.newInputStream(Paths.get(imgPath)));
        
        int newRed, newGreen, newBlue, grey;
        for (int row = 0; row < image.getWidth(); ++row) {
            for (int col = 0; col < image.getHeight(); ++col) {
                Color color = new Color(image.getRGB(row, col));
                
                grey = (int) Math.round(0.2126 * color.getRed())
                     + (int) Math.round(0.7152 * color.getGreen())
                     + (int) Math.round(0.0722 * color.getBlue());

                newRed      = (grey << 16) & 0x00FF0000;
                newGreen    = (grey << 8)  & 0x0000FF00;
                newBlue     =  grey        & 0x000000FF;
                
                //                      alpha   +   red     + green     + blue
                image.setRGB(row, col, 0xFF000000 | newRed | newGreen | newBlue);
            }
        }
        
        ImageIO.write(image, "jpg", Files.newOutputStream(Paths.get(imgPath)));
    }

}
