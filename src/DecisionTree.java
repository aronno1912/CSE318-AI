

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class DecisionTree
{

    public static TreeNode decisionTreeLearning(List<Map<String, String>> examples, List<String> attributes, List<Map<String, String>> parentExamples) {
        //List<String> remainingAttributes = new ArrayList<>(attributes); // Make a copy of attributes
        if (examples.isEmpty())
        {  // System.out.println("example empty");
            return pluralityValue(parentExamples);
        }
        else if (allExamplesHaveSameClassification(examples))
        {    //System.out.println("sameclass");
            TreeNode leaf = new TreeNode(null);
            leaf.classification = examples.get(0).get("classification");
            return leaf;
        }
        else if (attributes.isEmpty())
        {
           // System.out.println("att empty");
            return pluralityValue(examples);

        }
        else
        {
            String A = argMaxImportance(attributes, examples);
            TreeNode tree = new TreeNode(A);
            List<String> remainingAttributes = new ArrayList<>(attributes); // Make a copy of attributes
            remainingAttributes.remove(A);

            for (String v_k : getDistinctValues(A, examples)) {
                List<Map<String, String>> exs = filterExamples(A, v_k, examples);
                TreeNode subtree = decisionTreeLearning(exs, new ArrayList<>(remainingAttributes), examples);
                tree.children.put(A + " = " + v_k, subtree);
            }
            //System.out.println("hereeeeeeeee");
            return tree;
        }
    }


    public static TreeNode pluralityValue(List<Map<String, String>> examples) {
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
            if (entry.getValue() > maxCount) {
                mostCommonClassification = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        TreeNode leaf = new TreeNode(null);
        leaf.classification = mostCommonClassification;
        System.out.println("plu");
        return leaf;
    }

    public static boolean allExamplesHaveSameClassification(List<Map<String, String>> examples) {
        if (examples.isEmpty()) {
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
    public static Map<String, List<Map<String, String>>> splitExamplesByAttributeValue(String attribute, List<Map<String, String>> examples) {
        Map<String, List<Map<String, String>>> attributeValueExamplesMap = new HashMap<>();
        for (Map<String, String> example : examples) {
            String attributeValue = example.get(attribute);
            attributeValueExamplesMap.computeIfAbsent(attributeValue, k -> new ArrayList<>()).add(example);
        }
        return attributeValueExamplesMap;
    }

    public static double calculateEntropy(List<Map<String, String>> examples) {
        Map<String, Integer> classCounts = new HashMap<>();
        for (Map<String, String> example : examples) {
            String classification = example.get("classification");
            classCounts.put(classification, classCounts.getOrDefault(classification, 0) + 1);
        }

        double entropy = 0.0;
        int totalExamples = examples.size();
        for (String classification : classCounts.keySet()) {
            double probability = (double) classCounts.get(classification) / totalExamples;
            entropy -= probability * Math.log(probability) / Math.log(2); // Using base 2 logarithm
        }

        return entropy;
    }
    public static double calculateInformationGain(String attribute, List<Map<String, String>> examples) {
        double totalEntropy = calculateEntropy(examples);

        Map<String, List<Map<String, String>>> attributeValueExamplesMap = splitExamplesByAttributeValue(attribute, examples);

        double weightedEntropy = 0.0;
        for (String attributeValue : attributeValueExamplesMap.keySet()) {
            List<Map<String, String>> attributeExamples = attributeValueExamplesMap.get(attributeValue);
            double attributeFraction = (double) attributeExamples.size() / examples.size();
            weightedEntropy += attributeFraction * calculateEntropy(attributeExamples);
        }

        return totalEntropy - weightedEntropy;
    }

    public static String argMaxImportance(List<String> attributes, List<Map<String, String>> examples) {
        double maxInformationGain = Double.NEGATIVE_INFINITY;
        String bestAttribute = null;

        for (String attribute : attributes) {
            double informationGain = calculateInformationGain(attribute, examples);
            if (informationGain > maxInformationGain) {
                maxInformationGain = informationGain;
                bestAttribute = attribute;
            }
        }

        return bestAttribute;
    }

    /****
     *
     * This method iterates through the examples and collects the distinct attribute values for the specified attribute.
     * It uses a HashSet to ensure uniqueness of values and then converts the set back to an ArrayList before returning it.
     */
    public static List<String> getDistinctValues(String attribute, List<Map<String, String>> examples) {
        Set<String> distinctValues = new HashSet<>();
        for (Map<String, String> example : examples) {
            String attributeValue = example.get(attribute);
            distinctValues.add(attributeValue);
        }
        return new ArrayList<>(distinctValues);
    }

    public static List<Map<String, String>> filterExamples(String attribute, String value, List<Map<String, String>> examples) {
        List<Map<String, String>> filteredExamples = new ArrayList<>();

        for (Map<String, String> example : examples) {
            if (example.containsKey(attribute) && example.get(attribute).equals(value)) {
                filteredExamples.add(example);
            }
        }

        return filteredExamples;
    }

//    public static String classifyExample(TreeNode tree, Map<String, String> example) {
//        while (!tree.isLeaf())
//        {
//            String attribute = tree.getAttribute();
//            System.out.println(attribute);
//            String attributeValue = example.get(attribute);
//            System.out.println(attributeValue);
//            if (tree.hasChild(attributeValue)) {
//                tree = tree.getChild(attributeValue);
//                System.out.println("got one");
//            } else {
//                // If the attribute value is not found, return a default classification
//                return "unknown";
//            }
//        }
//        return tree.getClassification();
//    }

    public static String classifyExample(TreeNode tree, Map<String, String> example) {
        while (!tree.isLeaf()) {
            String attribute = tree.getAttribute();
           // System.out.println("Current attribute: " + attribute);

            String attributeValue = example.get(attribute);
            //System.out.println("Current attribute value: " + attributeValue);
            String check=attribute + " = " + attributeValue;
            if (tree.hasChild(check))
            {
                tree = tree.getChild(check);
               // System.out.println("Found child node for attribute value: " + attributeValue);
            } else {
                //System.out.println("Child node not found for attribute value: " + attributeValue);
                return "unknown";
            }
        }
        //System.out.println("Predicted classification: " + tree.getClassification());
        return tree.getClassification();
    }

    public static List<Map<String, String>> loadDatasetFromFile(String filename) {
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

    public static void printDecisionTree(TreeNode node, String indent) {
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

    public static void main(String[] args) {
        //String filePath = System.getProperty("user.dir") + "/input.txt";
        List<Map<String, String>> examples = loadDatasetFromFile("C:\\Users\\Lotus\\AI offlines\\src\\input.txt");
        List<String> attributes = Arrays.asList("buying", "maint", "doors", "persons", "lug_boot", "safety");
        // Shuffle the dataset randomly
        Collections.shuffle(examples);
        // Divide the data into training and testing sets (80-20 split)
        int splitIndex = (int) (examples.size() * 0.8);
        List<Map<String, String>> trainingExamples = examples.subList(0, splitIndex);
        List<Map<String, String>> testingExamples = examples.subList(splitIndex, examples.size());
        //System.out.println(testingExamples);

      /************************************ Train the decision tree ***************************************************/
        TreeNode root = decisionTreeLearning(trainingExamples, attributes, new ArrayList<>());
        //printDecisionTree(root, "");

//        // Classify new input data
//        Map<String, String> inputExample = new HashMap<>();
//        inputExample.put("buying", "vhigh");
//        inputExample.put("maint", "vhigh");
//        inputExample.put("doors", "2");
//        inputExample.put("persons", "2");
//        inputExample.put("lug_boot", "small");
//        inputExample.put("safety", "high");
//
//        String predictedClassification = classifyExample(root, inputExample);
//        System.out.println("Predicted classification: " + predictedClassification);

         //Test the decision tree on testing examples
        int correctPredictions = 0;
        for (Map<String, String> example : testingExamples) {
            String predictedClassification = classifyExample(root, example);
            String actualClassification = example.get("classification");
            System.out.println("predicted is "+predictedClassification);
            System.out.println("actual is "+actualClassification);
            if (predictedClassification.equals(actualClassification)) {
                correctPredictions++;
            }
        }

        // Calculate accuracy
        double accuracy = (double) correctPredictions / testingExamples.size() * 100;
        System.out.println("Accuracy: " + accuracy + "%");
    }


}

