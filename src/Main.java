
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class Main
{
    static Map<String, List<String>> allAttributeValues = new HashMap<>();
  /***************************    Train the tree  *******************************************************/
    public static TreeNode decisionTreeLearning(List<Map<String, String>> examples, List<String> attributes, List<Map<String, String>> parentExamples) {

        /**
         * If there are no examples left, it means that no example has been observed for this combination of attribute values, and we return a default value calculated from the plurality
         * classification of all the examples that were used in constructing the node’s parent. These
         * are passed along in the variable parent examples.
         * **/
        if (examples.isEmpty())
        {   //System.out.println("example empty");
            return pluralityValue(parentExamples);
        }
        /**
         * If the remaining examples are all positive (or all negative) [here it means same classification], then we are done: we can
         * answer it with a classification of leaf node
         * */
        else if (allExamplesHaveSameClassification(examples))
        {    //System.out.println("sameclass");
            TreeNode leaf = new TreeNode(null);
            leaf.classification = examples.get(0).get("classification");
            return leaf;
        }
        /**
         *  If there are no attributes left, but both positive and negative examples,
         * The best we can do is return the plurality classification of the remaining example**/
        // for noisy user input
        else if (attributes.isEmpty())
        {
            //System.out.println("att empty");
            return pluralityValue(examples);

        }
        /**If there are some positive and some negative examples, then choose the best attribute to
         split them. Recursive step!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * */
        else
        {
            //choose the most important attribute to expand the tree
            String A = argMaxImportance(attributes, examples);
            TreeNode tree = new TreeNode(A);
            List<String> remainingAttributes = new ArrayList<>(attributes); // Make a copy of attributes
            remainingAttributes.remove(A);

            for (String valuesOfThatAttr : allAttributeValues.get(A))
            {
                //exs is the new example set on which a new subtree will be constructed
                //for each value valuesOfThatAttr of attribute A,search through the whole example set anf find out the examples that which go this whose
                List<Map<String, String>> subtreeExamples = filterExamples(A, valuesOfThatAttr, examples);
                //for each value ,construct a subtree[ like construct 3 subtree for patron=none,patron=some,patron=many]
                TreeNode subtree = decisionTreeLearning(subtreeExamples, new ArrayList<>(remainingAttributes), examples);
                tree.children.put(A + " = " + valuesOfThatAttr, subtree);
            }
            //System.out.println("hereeeeeeeee");
            return tree;
        }
    }


    public static TreeNode pluralityValue(List<Map<String, String>> examples)
    {
        Map<String, Integer> classificationCount = new HashMap<>();

        // Count the occurrences of each classification
        for (Map<String, String> example : examples) {
            String classification = example.get("classification");
            classificationCount.put(classification, classificationCount.getOrDefault(classification, 0) + 1);
        }

        // Find the classification with the highest count
        String mostCommonClassification = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : classificationCount.entrySet()) {
            if (entry.getValue() > maxCount)
            {
                mostCommonClassification = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        TreeNode leaf = new TreeNode(null);
        leaf.classification = mostCommonClassification;
        //System.out.println("plu");
        return leaf;
    }
  //leaf
    public static boolean allExamplesHaveSameClassification(List<Map<String, String>> examples)
    {
        if (examples.isEmpty())
        {
            return false; // No examples, so they don't have the same classification
        }

        String firstClassification = examples.get(0).get("classification");

        for (int i = 1; i < examples.size(); i++) {
            if (!examples.get(i).get("classification").equals(firstClassification)) {
                return false; // Found an example with a different classification
            }
        }

        return true; // All examples have the same classification
    }
    /**
     * takes an attribute name and a list of examples (data points) as input. Its purpose is to split the list of examples into
     * sublists based on the values of the specified attribute. It returns a map where the keys are the distinct attribute values, and the
     * corresponding values are lists of examples that have that attribute value.*/
    //basically (in mind)constructs subtrees for different values of an attribute... like door=2,door=2,door=4 [for calculating info gain..not actually constructs subtrees]
    public static Map<String, List<Map<String, String>>>
    splitExamplesByAttributeValue(String attribute, List<Map<String, String>> examples)
    {
        Map<String, List<Map<String, String>>> attributeValueExamplesMap = new HashMap<>();
        for (Map<String, String> example : examples) {
            String attributeValue = example.get(attribute);
            attributeValueExamplesMap.computeIfAbsent(attributeValue, k -> new ArrayList<>()).add(example);
        }
        return attributeValueExamplesMap;
    }

    public static double calculateEntropy(List<Map<String, String>> examples)
    {
        Map<String, Integer> counterOfClassifiations = new HashMap<>();  //to store the counts of each unique classification present in the examples.
        for (Map<String, String> example : examples)
        {
            String classification = example.get("classification");
            //if the class label is already in the map. If it is, it increments the count by 1. If it's not, it initializes the count to 1.
            counterOfClassifiations.put(classification, counterOfClassifiations.getOrDefault(classification, 0) + 1);
        }

        double entropy = 0.0;
        int totalExamples = examples.size();
        for (String classification : counterOfClassifiations.keySet())
        {
            double probability = (double) counterOfClassifiations.get(classification) / totalExamples;
            entropy -= probability * Math.log(probability) / Math.log(2); // Using base 2 logarithm
        }

        return entropy;
    }
    //calculation of information gain
    public static double calculateInformationGain(String attribute, List<Map<String, String>> examples)
    {
        double totalEntropy = calculateEntropy(examples);

        Map<String, List<Map<String, String>>> attributeValueExamplesMap = splitExamplesByAttributeValue(attribute, examples);

        double weightedEntropy = 0.0;
        for (String attributeValue : attributeValueExamplesMap.keySet())
        {
            List<Map<String, String>> attributeExamples = attributeValueExamplesMap.get(attributeValue);
            double attributeFraction = (double) attributeExamples.size() / examples.size();
            weightedEntropy += attributeFraction * calculateEntropy(attributeExamples);
        }

        return totalEntropy - weightedEntropy;
    }

    //choose that attribute whose information gain is highest!!!!!!!!!!!!!!!!!!!!

    public static String argMaxImportance(List<String> attributes, List<Map<String, String>> examples) {
        double maxInformationGain = Double.NEGATIVE_INFINITY;
        String bestAttribute = null;

        for (String attribute : attributes)
        {
            double informationGain = calculateInformationGain(attribute, examples);
            if (informationGain > maxInformationGain)
            {
                maxInformationGain = informationGain;
                bestAttribute = attribute;
            }
        }

        return bestAttribute;
    }

    public static String classifyExample(TreeNode tree, Map<String, String> example)
    {
        //traverse  the tree to get the classification
        while (!tree.isLeaf())
        {
            //returns attribute of a node
            String attribute = tree.getAttribute();
            // System.out.println("Current attribute: " + attribute);

            String attributeValue = example.get(attribute);
            //System.out.println("Current attribute value: " + attributeValue);
            String check=attribute + " = " + attributeValue;
            tree = tree.getChild(check);
                // System.out.println("Found child node for attribute value: " + attributeValue);
        }
        return tree.getClassification();
    }


    //takes an attribute name, an attribute value, and a list of examples (data points) as input. Its purpose is to
    // filter the list of examples and return a new list containing only the examples that have a specific attribute value for the specified attribute.
    public static List<Map<String, String>>
    filterExamples(String attribute, String value, List<Map<String, String>> examples)
    {
        List<Map<String, String>> filteredExamples = new ArrayList<>();

        for (Map<String, String> example : examples) {
            if (example.containsKey(attribute) && example.get(attribute).equals(value)) {
                filteredExamples.add(example);
            }
        }

        return filteredExamples;
    }

//       / * This method iterates through the examples and collects the distinct attribute values for the specified attribute.
//     * It uses a HashSet to ensure uniqueness of values and then converts the set back to an ArrayList before returning it.
//     */
    /**   if manually didn't want to store all attributes values...only works with examples attributes values              **/
    public static List<String> getDistinctValues(String attribute, List<Map<String, String>> examples) {
        Set<String> distinctValues = new HashSet<>();
        for (Map<String, String> example : examples) {
            String attributeValue = example.get(attribute);
            distinctValues.add(attributeValue);
        }
        return new ArrayList<>(distinctValues);
    }


    public static List<Map<String, String>> loadDatasetFromFile(String filename) {
        //attribute,values
        List<Map<String, String>> examples = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 7) { // Assuming there are 7 attributes + classification
                    Map<String, String> example = new HashMap<>();
                    example.put("buying", values[0]);
                    example.put("maint", values[1]);
                    example.put("doors", values[2]);
                    example.put("persons", values[3]);
                    example.put("lug_boot", values[4]);
                    example.put("safety", values[5]);
                    example.put("classification", values[6]);
                    examples.add(example);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return examples;
    }
// for debugging
    public static void printDecisionTree(TreeNode node, String indent)
    {
        if (node == null) {
            return;
        }

        if (node.isLeaf()) {
            System.out.println(indent + "Leaf: " + node.getClassification());
            return;
        }

        System.out.println(indent + "Attribute: " + node.getAttribute());
        for (Map.Entry<String, TreeNode> entry : node.children.entrySet()) {
            System.out.println(indent + "  " + entry.getKey());
            printDecisionTree(entry.getValue(), indent + "    ");
        }
    }

    // s.d= sqrt((each value from the population - mean)^2/N)
    public static double calculateStandardDeviation(double[] values) {
        double mean = Arrays.stream(values).average().getAsDouble();
        double sumSquaredDiff = Arrays.stream(values).map(v -> Math.pow(v - mean, 2)).sum();
        double variance = sumSquaredDiff / values.length;
        return Math.sqrt(variance);
    }

public static void main(String[] args)
{
    // Load the dataset from the file
    /**  Made a list of hashmaps
     * [buying-->vhigh, maint-->vhigh, doors-->2, persons-->2, lug_boot-->low, safety-->high, classification-->acc], [.......]
     * **/
    List<Map<String, String>> examples = loadDatasetFromFile("C:\\Users\\Lotus\\AI offlines\\src\\input.txt");
    List<String> attributes = Arrays.asList("buying", "maint", "doors", "persons", "lug_boot", "safety");
    // Define the possible values for each attribute
    List<String> buyingValues = new ArrayList<>();
    buyingValues.add("vhigh");
    buyingValues.add("high");
    buyingValues.add("med");
    buyingValues.add("low");

    List<String> maintValues = new ArrayList<>();
    maintValues.add("vhigh");
    maintValues.add("high");
    maintValues.add("med");
    maintValues.add("low");

    List<String> doorsValues = new ArrayList<>();
    doorsValues.add("2");
    doorsValues.add("3");
    doorsValues.add("4");
    doorsValues.add("5more");

    List<String> personsValues = new ArrayList<>();
    personsValues.add("2");
    personsValues.add("4");
    personsValues.add("more");

    List<String> lugBootValues = new ArrayList<>();
    lugBootValues.add("small");
    lugBootValues.add("med");
    lugBootValues.add("big");

    List<String> safetyValues = new ArrayList<>();
    safetyValues.add("low");
    safetyValues.add("med");
    safetyValues.add("high");

    // Put attribute values in the HashMap
    allAttributeValues.put("buying", buyingValues);
    allAttributeValues.put("maint", maintValues);
    allAttributeValues.put("doors", doorsValues);
    allAttributeValues.put("persons", personsValues);
    allAttributeValues.put("lug_boot", lugBootValues);
    allAttributeValues.put("safety", safetyValues);

    // Define the number of runs for the experiment
    int numRuns = 20;

    // Initialize arrays to store accuracy values
     double[] accuraciesArray = new double[numRuns];

    // Repeat the experiment for the specified number of runs
    for (int run = 0; run < numRuns; run++)
    {
       /**   Shuffle randomly and then divide into 80% for training set and 20% for testSet  *********/
        Collections.shuffle(examples);

        // Determine the split index for training and testing sets
        int splitIndex = (int) (examples.size() * 0.8); // 80% for training

        // Split the dataset into training and testing sets
        List<Map<String, String>> trainingExamples = examples.subList(0, splitIndex);
        List<Map<String, String>> testingExamples = examples.subList(splitIndex, examples.size());

        // Train the decision tree
        TreeNode root = decisionTreeLearning(trainingExamples, attributes, new ArrayList<>());

        // Test the decision tree on testing examples
        int correctPredictions = 0;
        for (Map<String, String> example : testingExamples)
        {
            String predictedClassification = classifyExample(root, example);
            String actualClassification = example.get("classification");
            //System.out.println("predicted is "+predictedClassification);
            //System.out.println("actual is "+actualClassification);
            if (predictedClassification.equals(actualClassification)) {
                correctPredictions++;
            }
        }

        // Calculate accuracy for the current run
        double accuracy = (double) correctPredictions / testingExamples.size() * 100;
        accuraciesArray[run] = accuracy;
        int printrun=run+1;
        System.out.println("Run "+printrun+" complete!!!");
        //System.out.println("Accuracy in  "+printrun+" run is "+accuraciesArray[run]+"%");
    }

    // Calculate mean accuracy and standard deviation
    double meanAccuracy = Arrays.stream(accuraciesArray).average().getAsDouble();
    double stdDeviation = calculateStandardDeviation(accuraciesArray);

    // Print the results
    System.out.println();
    System.out.println("Mean Accuracy: " + meanAccuracy + "%");
    System.out.println("Standard Deviation of Accuracy: " + stdDeviation);
}


}

