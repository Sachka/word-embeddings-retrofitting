import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Comparator;
//TODO translate
//CUR closing functions
public class WordDistance {
    //*Imperative code
    // COSINE SIMILARITY FUNCTION
    public static double cosineSimilarity(double[] coords1, double[] coords2) {
        double product = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        double similarity = 0.0;
        for (int i = 0; i < coords1.length; i++) {
            product += coords1[i] * coords2[i];
            magnitude1 += Math.pow(coords1[i], 2);
            magnitude2 += Math.pow(coords2[i], 2);
        }
        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);
        if (magnitude1 != 0.0 || magnitude2 != 0.0) {
            similarity = product / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
        return similarity;
    }
    // WORDSET FILE HANDLER
    public static HashMap<String, double[]> wordVectSect(String vectors, int... dimensions) throws IOException {
        // Initialization
        HashMap<String, double[]> wordVect;
        try (BufferedReader wordVectFileBuffer = new BufferedReader(new FileReader(vectors))) {
            wordVect = new HashMap<String, double[]>();
            // WordVect header
            // words dimensions "^\d+\s\d+$"
            String firstLine = wordVectFileBuffer.readLine();
            String[] wordVectHeader = firstLine.split(" ");
            int totalNumberOfwords = Integer.valueOf(wordVectHeader[0]);
            int totalNumberOfDimensions = Integer.valueOf(wordVectHeader[1]);
            if (dimensions.length > 0 && dimensions[0] <= totalNumberOfDimensions) totalNumberOfDimensions = dimensions[0];
            //System.out.println(totalNumberOfDimensions);
            String wordVectLine = wordVectFileBuffer.readLine();
            String[] wordVectArray = wordVectLine.split(" ");
            //int t = totalNumberOfwords;
            for (int h = 0; h < totalNumberOfwords - 1; h++) {
                wordVectLine = wordVectFileBuffer.readLine();
                wordVectArray = wordVectLine.split(" ");
                String word = wordVectArray[0];
                wordVectArray = deleteWordFromWordVectLine(wordVectArray, word);
                double[] coordinates = new double[totalNumberOfDimensions];
                //for (int i = 0; i < wordVectArray.length -1; i++) {
                for (int i = 0; i < totalNumberOfDimensions-1; i++) {
                    coordinates[i] = Double.parseDouble(wordVectArray[i]);
                }
                wordVect.put(word, coordinates);
            }
            return wordVect;
        }
        catch (FileNotFoundException fileNotFound) {
            System.out.println("Typo? File not found.");
            throw fileNotFound;
        }
    }
    // ANALOGY TEST (Ce qui est à 'A' ce que 'B' est à 'C')
    public static HashMap<Double,String> analogyAnalysis(HashMap<String, double[]> wordVectSet, String wordA, String wordB, String wordC, int... dimensions) {
        int updatedDimensions = 50;
        if (dimensions.length > 0) updatedDimensions = dimensions[0];
        double[] vectorOfWordA = ((double[]) wordVectSet.get(wordA));
        double[] vectorOfWordB = ((double[]) wordVectSet.get(wordB));
        double[] vectorOfWordC = ((double[]) wordVectSet.get(wordC));
        // u and v are temporary arrays to test the analogy (i)
        double[] u = new double[updatedDimensions];
        double[] v = new double[updatedDimensions];
        HashMap<Double, String> resultat = new HashMap<Double, String>();
        for (int i = 0; i < updatedDimensions; i++) { // CALCULE MATHEMATIQUE DE L'ANALOGIE
            u[i] = vectorOfWordA[i] - vectorOfWordB[i]; // A - B
        }
        for (int i = 0; i < updatedDimensions; i++) {
            v[i] = vectorOfWordC[i] + u[i]; // A - B + C
        }
        for (Map.Entry<String, double[]> entry : wordVectSet.entrySet()) {
            if (cosineSimilarity(v, (double[]) entry.getValue()) != 0.0) {
                resultat.put(cosineSimilarity(v, (double[]) entry.getValue()),entry.getKey());
            }
        }
        return resultat;
    }
    // REDUCTION ANALYSIS
    public static HashMap<Double,String> meaningReductionAnalysis(HashMap<String, double[]> wordVectSet, String wordA, String wordB, int... dimensions) {
        double[] vectorOfWordA = ((double[]) wordVectSet.get(wordA));
        double[] vectorOfWordB = ((double[]) wordVectSet.get(wordB));
        int addedDimensions = 50;
        if (dimensions[0] > 0) addedDimensions = dimensions[0];
        // u and v are temporary arrays to test the analogy (i)
        double[] u = new double[addedDimensions];
        HashMap<Double, String> resultat = new HashMap<Double, String>();
        for (int i = 0; i < addedDimensions; i++) { // CALCULE MATHEMATIQUE DE LA REDUCTION
            u[i] = vectorOfWordA[i] - vectorOfWordB[i]; // A - B
        }
        for (Map.Entry<String, double[]> entry : wordVectSet.entrySet()) {
            if (cosineSimilarity(u, (double[]) entry.getValue()) != 0.0) {
                resultat.put(cosineSimilarity(u, (double[]) entry.getValue()),entry.getKey());
            }
        }
        return resultat;
    }

    // AUGMENTATION ANALYSIS
    public static HashMap<Double,String> meaningAugmentationAnalysis(HashMap<String, double[]> wordVectSet, String wordA, String wordB, int... dimensions) {
        double[] vectorOfWordA = ((double[]) wordVectSet.get(wordA));
        double[] vectorOfWordB = ((double[]) wordVectSet.get(wordB));
        int addedDimensions = 50;
        if (dimensions.length > 0) addedDimensions = dimensions[0];
        // u and v are temporary arrays to test the analogy (i)
        double[] u = new double[addedDimensions];
        HashMap<Double, String> resultat = new HashMap<Double, String>();
        for (int i = 0; i < addedDimensions; i++) { // CALCULE MATHEMATIQUE DE LA AUGMENTATION
            u[i] = vectorOfWordA[i] + vectorOfWordB[i]; // A + B
        }
        for (Map.Entry<String, double[]> entry : wordVectSet.entrySet()) {
            if (cosineSimilarity(u, (double[]) entry.getValue()) != 0.0) {
                resultat.put(cosineSimilarity(u, (double[]) entry.getValue()),entry.getKey());
            }
        }
        return resultat;
    }

    // SIMILARITY TEST
    public static HashMap<Double,String> similarityAnalysis(HashMap<String,double[]> wordVectSet, String word) throws NullPointerException {
        try {
            double[] vectorOfWord = ((double[]) wordVectSet.get(word));
            HashMap<Double, String> resultat = new HashMap<Double, String>(); // RETOUR HASHTAG de (KEY) coordonnées et (VALUE) mots similaires
            for (Map.Entry<String, double[]> entry : wordVectSet.entrySet()) {
                if (cosineSimilarity(vectorOfWord, (double[]) entry.getValue()) != 1.0) { // CALCUL DE MOTS SIMILAIRES
                    resultat.put(cosineSimilarity(vectorOfWord, (double[]) entry.getValue()),entry.getKey()); // ENSEMBLE DE RESULTATS
                }
            }
            return resultat;
        } catch (NullPointerException unknownWord) {
            System.out.println("Nope, that word is not in this network.");
            throw unknownWord;
        }
    }

    // SORTING OF CONNECTIONS
    public static String topScore (HashMap<Double,String> resultat, int... resultValues) {
        String string = "";
        int topValue = 1;
        int bottomValue = 0;
        if (resultValues.length == 1) topValue = resultValues[0];
        if (resultValues.length > 1) topValue = topValue + resultValues[1];
        Map<Double, String> topScore = new TreeMap<Double, String>(new Comparator<Double>() {public int compare(Double o1, Double o2) {return o2.compareTo(o1);}});
        topScore.putAll(resultat); // ECRITURE DES RESULTATS DANS UN ORDRE DECROISSANT
        List<Double> pointList = new ArrayList<Double>(topScore.keySet());
        List<String> nameList = new ArrayList<String>(topScore.values());
        List<Double> topDistances = pointList.subList(bottomValue, topValue); // ***
        List<String> topNames = nameList.subList(bottomValue, topValue); // *** SPLIT SUB LISTE DE TOP 8
        for (int i = 0; i < topDistances.size(); i++) {
            string = string + "\"" + topNames.get(i) +"\""+ ", sharing " + Math.round(topDistances.get(i) * 100) + "% of its semantic meaning.\n";
        }
        string = string.trim();
        return string;
    }

    //**FONCTION EXTRA POUR ENLEVER LE MOT DES COORDONNEES DURANT LA LECTURE**
    public static String[] deleteWordFromWordVectLine(String[] ligne, String word) { // RACOURCI POUR FACILITER LA CREATION DU HASHMAP
        if (ligne == null) {
            return null;
        } else if (ligne.length <= 0) {
            return ligne;
        } else {
            String[] propre = new String[ligne.length - 1];
            int compteur = 0;
            for (String i : ligne) {
                if (!i.equals(word)) {
                    propre[compteur++] = i;
                }
            }
            return propre;
        }
    }
    public static String replaceLast(String text, String regex, String replacement) {
        return (text.replaceFirst("(?s).*"+regex, "$1" + replacement));
    }

// MAIN PROGRAM
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String usage = "Usage: simcalc [-d (for number of dimensions)] [NWE Corpus file]";
        boolean atLoadingMenu = true;
// LOADING MENU
        while(atLoadingMenu) {
            try {
                System.out.println("Similarity Calculator.");
                String directory = System.getProperty("user.dir");
                //System.out.println(directory); // debug directory
                Scanner inputScanner = new Scanner(System.in);
                String wordSet = "";
                int dimensions = 50;
                if (args.length == 0) {
                    System.out.println("Please load a neural word embedding corpus file");
                    System.out.print("> ");
                    wordSet = inputScanner.nextLine();
                    if (wordSet.contains("\\e")) {
                        System.out.println("Program terminated.");
                        break;
                    }
                } else {
                    wordSet = args[args.length - 1];
                    try {
                        BufferedReader buffer = new BufferedReader(new FileReader(args[args.length - 1]));
                        String firstLine = buffer.readLine();
                        String[] split = firstLine.split(" ");
                        dimensions = Integer.parseInt(split[1]);
                        for (int i = 0; i < args.length; i++) if (args[i].equals("-d")) dimensions = Integer.parseInt(args[i + 1]);
                    } catch (FileNotFoundException err) {
                        System.out.println(usage);
                        break;
                    } catch (NumberFormatException error) {
                        System.out.println(usage);
                        break;
                    }
                }
                boolean atSelectionMenu = true;
                String[] wordSetAndDimensions = wordSet.split(" ");
                if (wordSetAndDimensions.length > 1) dimensions = Integer.parseInt(wordSetAndDimensions[1]);
                System.out.println("Loading " +  wordSetAndDimensions[0] +  "...");
                HashMap<String,double[]> corpus = wordVectSect(directory + "/" + wordSetAndDimensions[0], dimensions);
                String inputOption = "option";
                System.out.println(wordSet + " loaded correctly with " + dimensions + " dimensions of depth." );
                System.out.println("Semantic similarity, reduction, enrichment and analogy options are enabled.");
                System.out.println("Play with the meaning of words.");
// SELECTION MENU
                while (atSelectionMenu) {
                    try {
                        //System.out.println("Select an option.");
                        //System.out.print("> ");
                        inputOption = "m";
                        //if (inputOption.contains("\\c")) {
                            //atSelectionMenu = false;
                            //System.out.println("Type in a new neural-network corpus.");
                            //inputOption = "change()";
                        //}
                        //else if (inputOption.contains("\\e")) {
                            //System.out.println("Program terminated.");
                            //atSelectionMenu = false;
                            //atLoadingMenu = false;
                        //}
                        //else if (inputOption.contains("\\b")) {
                            //atSelectionMenu = true;
                            //inputOption = "\\c";
                        //}
// SIMILARITY MENU (deprecated)
                        //while (inputOption.charAt(0) == 's') {
                            //boolean atSimilarityMenu = true;
                            //while (atSimilarityMenu) {
                                //try {
                                    //System.out.println("Test a word.");
                                    //System.out.print("> ");
                                    //String inputSentence = inputScanner.nextLine();
                                    //String[] wordsAndExpectedNumberOfResults = inputSentence.split(" ");
                                    //String word = wordsAndExpectedNumberOfResults[0];
                                    //int userExpectedNumberOfResults = 1;
                                    //if (word.contains("\\b")) {
                                        //atSimilarityMenu = false;
                                        //inputOption = word;
                                    //} else if (word.contains("\\e")) {
                                        //System.out.println("Program terminated.");
                                        //atSimilarityMenu = false;
                                        //atSelectionMenu = false;
                                        //atLoadingMenu = false;
                                        //inputOption = word;
                                    //} else {
                                        //if (wordsAndExpectedNumberOfResults.length > 1) userExpectedNumberOfResults = Integer.parseInt(wordsAndExpectedNumberOfResults[1]);
                                        //HashMap<Double,String> similtest = new HashMap<Double,String>();
                                        //System.out.println("Thinking...");
                                        //similtest = similarityAnalysis(corpus, word);
                                        //String resulta = topScore(similtest, userExpectedNumberOfResults);
                                        //String resulta2 = secondTop10(similtest, userExpectedNumberOfResults);
                                        //if (resulta.contains(word)) {
                                            //System.out.println("\"" + word.replaceAll("_", " ") + "\"" + " real position is slightly out of place, it is most similar to:\n" + resulta2);
                                        //} else {
                                            //System.out.println("\"" + word.replaceAll("_", " ") + "\"" + " is similar to:\n " + resulta);
                                            //}
                                        //}
                                    //} catch (NullPointerException uWord) {
                                         //System.out.println("Try another word.");
                                //} catch (ArrayIndexOutOfBoundsException outOfThisWorld) {
                                        //System.out.println("Syntax error, check phrase structure.");
                                //} catch (NumberFormatException thatWasNotANumber) {
                                        //System.out.println("Syntax error, rightmost element must be an integer.");
                                //} catch (IndexOutOfBoundsException tooShort) {
                                        //System.out.println("This word doesn't have any connections.");
                                //}
                            //}
                        //}
// COMPLETEMODE MENU
                        while (inputOption.charAt(0) == 'm') {
                            boolean atCompleteModeMenu = true;
                            while (atCompleteModeMenu) {
                                try {
                                    //System.out.println("Complete analysis of words.");
                                    System.out.print("> ");
                                    String inputSentence = inputScanner.nextLine();
                                    if (inputSentence.matches(".+\\d")) inputSentence = inputSentence.replaceAll("(.+) (\\d+)", "$1#$2");
                                    inputSentence = inputSentence.replaceAll(" ","_").replaceAll("_\\+_", " \\+ ").replaceAll("_-_", " - ").replaceAll("#", " ");
                                    String[] wordsAndExpectedNumberOfResults = inputSentence.split(" ");
                                    String word = wordsAndExpectedNumberOfResults[0];
                                    int userExpectedNumberOfResults = 1;
                                    int resultRepetitionCounter = 0;
                                    int resultCounter = 0;
                                    if (word.contains("\\c")) {
                                        atCompleteModeMenu = false;
                                        atSelectionMenu = false;
                                        atLoadingMenu = true;
                                        inputOption = "c";
                                        System.out.println("Type in a new neural-network corpus.");
                                    } else if (word.contains("\\e")) {
                                        System.out.println("Program terminated.");
                                        atCompleteModeMenu = false;
                                        atSelectionMenu = false;
                                        atLoadingMenu = false;
                                        inputOption = inputSentence;
                                    } else {
                                        // Analogy
                                        if (wordsAndExpectedNumberOfResults.length >= 5) {
                                            final int topThreeResults = 3;
                                            word = wordsAndExpectedNumberOfResults[2];
                                            String secondWord = wordsAndExpectedNumberOfResults[0];
                                            String thirdWord = wordsAndExpectedNumberOfResults[4];
                                            if (wordsAndExpectedNumberOfResults.length > 5) userExpectedNumberOfResults = Integer.parseInt(wordsAndExpectedNumberOfResults[5]);
                                            HashMap<Double,String> similtest = new HashMap<Double,String>();
                                            System.out.println("Thinking...");
                                            similtest = analogyAnalysis(corpus, word, secondWord, thirdWord, dimensions);
                                            // top 3 words
                                            String testResult = topScore(similtest, topThreeResults);
                                            // three input words
                                            String[] resultRepetitionArray = {word, secondWord, thirdWord};
                                            for (String notWanted : resultRepetitionArray) if (testResult.contains(notWanted)) resultRepetitionCounter++;
                                            String realResult = topScore(similtest, userExpectedNumberOfResults+ resultRepetitionCounter);
                                            for (String notWanted : resultRepetitionArray) realResult = realResult.replaceAll("(\n)?\""+notWanted+"\", sharing \\d+% of its semantic meaning\\.(\n)?", "\n");
                                            for (int i = 0; i < realResult.length(); i++) if (realResult.charAt(i) == '%') resultCounter++;
                                            if (resultCounter > userExpectedNumberOfResults) realResult = realResult.replaceAll("(\n)\".*\", sharing \\d+% of its semantic meaning\\.(\n)?$", "\n");
                                            realResult = realResult.trim();
                                            System.out.println("\""+ secondWord.replaceAll("_", " ") + "\""+ " is to " + "\"" + word.replaceAll("_", " ") + "\"" + " as " + "\"" + thirdWord.replaceAll("_", " ") + "\"" + " is to:\n" + realResult.replaceAll("_"," "));
                                        }
                                        // Reduction
                                        else if (wordsAndExpectedNumberOfResults.length >= 3 && wordsAndExpectedNumberOfResults[1].matches(".*(-|minus|out)")) {
                                            final int topTwoResults = 2;
                                            String secondWord = wordsAndExpectedNumberOfResults[2];
                                            if (wordsAndExpectedNumberOfResults.length > 3) userExpectedNumberOfResults = Integer.parseInt(wordsAndExpectedNumberOfResults[3]);
                                            HashMap<Double,String> similtest = new HashMap<Double,String>();
                                            System.out.println("Thinking...");
                                            similtest = meaningReductionAnalysis(corpus, word, secondWord, dimensions);
                                            // top 2 words
                                            String testResult = topScore(similtest, userExpectedNumberOfResults);
                                            // two input words
                                            String[] inputWordsArray = {word, secondWord};
                                            for (String notWanted : inputWordsArray) if (testResult.contains(notWanted)) resultRepetitionCounter++;
                                            String realResult = topScore(similtest, userExpectedNumberOfResults+ resultRepetitionCounter);
                                            for (String notWanted : inputWordsArray) realResult = realResult.replaceAll("(\n)?\""+notWanted+"\", sharing \\d+% of its semantic meaning\\.(\n)?", "\n");
                                            for (int i = 0; i < realResult.length(); i++) if (realResult.charAt(i) == '%') resultCounter++;
                                            if (resultCounter > userExpectedNumberOfResults) realResult = realResult.replaceAll("(\n)\".*\", sharing \\d+% of its semantic meaning\\.(\n)?$", "\n");
                                            realResult = realResult.trim();
                                            System.out.println("\"" + word.replaceAll("_", " ") + "\""+ " without the meaning of " + "\""+ secondWord.replaceAll("_", " ") + "\"" + " is similar to:\n" + realResult.replaceAll("_", " "));
                                        }
                                        // Enrichment
                                        else if (wordsAndExpectedNumberOfResults.length >= 3) {
                                            final int topTwoResults = 2;
                                            String secondWord = wordsAndExpectedNumberOfResults[2];
                                            if (wordsAndExpectedNumberOfResults.length > 3) userExpectedNumberOfResults = Integer.parseInt(wordsAndExpectedNumberOfResults[3]);
                                            HashMap<Double,String> similtest = new HashMap<Double,String>();
                                            System.out.println("Thinking...");
                                            similtest = meaningAugmentationAnalysis(corpus, word, secondWord, dimensions);
                                            // top 2 words.
                                            String testResult = topScore(similtest, userExpectedNumberOfResults+1);
                                            // two input words
                                            String[] inputWordsArray = {word, secondWord};
                                            for (String notWanted : inputWordsArray) if (testResult.contains(notWanted)) resultRepetitionCounter++;
                                            String realResult = topScore(similtest, userExpectedNumberOfResults+ resultRepetitionCounter);
                                            for (String notWanted : inputWordsArray) realResult = realResult.replaceAll("(\n)?\""+notWanted+"\", sharing \\d+% of its semantic meaning\\.(\n)?", "\n");
                                            for (int i = 0; i < realResult.length(); i++) if (realResult.charAt(i) == '%') resultCounter++;
                                            if (resultCounter > userExpectedNumberOfResults) realResult = realResult.replaceAll("(\n)\".*\", sharing \\d+% of its semantic meaning\\.(\n)?$", "\n");
                                            realResult = realResult.trim();
                                            System.out.println("\"" + word.replaceAll("_", " ") + "\""+ " enriched with the meaning of " + "\""+ secondWord.replaceAll("_", " ") + "\"" + " is similar to:\n" + realResult.replaceAll("_", " "));
                                        }
                                        // Similarity
                                        else {
                                            if (wordsAndExpectedNumberOfResults.length > 1) userExpectedNumberOfResults = Integer.parseInt(wordsAndExpectedNumberOfResults[1]);
                                            HashMap<Double,String> similtest = new HashMap<Double,String>();
                                            System.out.println("Thinking...");
                                            similtest = similarityAnalysis(corpus, word);
                                            // top word.
                                            String testResult = topScore(similtest);
                                            if (testResult.contains(word)) resultRepetitionCounter++;
                                            String realResult = topScore(similtest, userExpectedNumberOfResults+ resultRepetitionCounter);
                                            if (testResult.contains(word)) realResult = realResult.replaceAll("(\n)?\"" + word.replaceAll("_", " ") + "\", sharing \\d+% of its semantic meaning\\.(\n)?", "");
                                            System.out.println("\"" + word.replaceAll("_", " ") + "\"" + " is similar to:\n" + realResult.replaceAll("_", " "));
                                        }
                                    }
                                } catch (NullPointerException uWord) {
                                         System.out.println("Try another set of words.");
                                } catch (ArrayIndexOutOfBoundsException outOfThisWorld) {
                                        System.out.println("Syntax error, check phrase structure.");
                                } catch (NumberFormatException thatWasNotANumber) {
                                        System.out.println("Syntax error, rightmost element must be an integer when enough words are parsed.");
                                } catch (IndexOutOfBoundsException tooShort) {
                                        System.out.println("This word doesn't have any connections.");
                                }
                            }
                        }
// END OF COMPLETEMODE MENU
                    } catch (StringIndexOutOfBoundsException error) {
                        System.out.println("Wrong option.");
                    }
                }
// END OF SELECTION MENU
                if (inputOption.charAt(0) == 'c')  atLoadingMenu = true;
                else {
                    atLoadingMenu = false;
                    inputScanner.close();
                }
            } catch (FileNotFoundException error) {
                System.out.println("Try again.");
            } catch (NumberFormatException error) {
                System.out.println("This corpus file is not correct.");
            } catch (NullPointerException error) {
                System.out.println("This corpus file is not correct.");
            }
        }
// END OF LOADING MENU
    }
// END OF MAIN PROGRAM
}
// END OF CLASS
