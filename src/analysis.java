//import si.lj.uni.fri.lna.test.bib.Graph;

import java.io.IOException;
import java.util.*;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

public class analysis {
    public static HashMap<String, Double> prepare_weights(Graph graph, Double set_x) {
        HashMap<String, Double> weights = new HashMap<>();

        int[] labels = graph.getLabels();
        for (int label: labels) {
            int node = graph.getNode(label);
            for (int successor: graph.getSuccessors(node)) {
                String edge = Integer.toString(node) + "-" + Integer.toString(successor);
                weights.put(edge, set_x);
            }
        }


        return weights;
    }

    private static void print(Graph graph, List<Integer> nodes, final double[] intermediacy, int source, int target, int samples) {
        Collections.sort(nodes, new Comparator<Integer>() {
            @Override
            public int compare(Integer first, Integer second) {
                if (intermediacy[first] == intermediacy[second])
                    return new Integer(first).compareTo(second);

                return -new Double(intermediacy[first]).compareTo(intermediacy[second]);
            }
        });

        source = graph.getNode(source);
        target = graph.getNode(target);

        System.out.println(String.format("\n%15s | %s", "Intermediacy", "..."));
        System.out.printf(String.format("%15s | %.30f ± %.30f", "Source", intermediacy[source], Math.sqrt(intermediacy[source] * (1.0 - intermediacy[source]) / samples)));
        System.out.println(" | " + graph.getNodeAttributes(source).toString());
        System.out.printf(String.format("%15s | %.30f ± %.30f", "Target", intermediacy[target], Math.sqrt(intermediacy[target] * (1.0 - intermediacy[target]) / samples)));
        System.out.println(" | " + graph.getNodeAttributes(target).toString());

        for (int i = 0; i < Math.min(25, nodes.size()); i++) {
            System.out.printf(String.format("%15s | %.30f ± %.30f", "'" + graph.getLabel(nodes.get(i)) + "'", intermediacy[nodes.get(i)], 1.96 * Math.sqrt(intermediacy[nodes.get(i)] * (1.0 - intermediacy[nodes.get(i)]) / samples)));
            System.out.println(" | " + graph.getNodeAttributes(graph.getNode(graph.getLabel(nodes.get(i)))).toString());
        }
    }

    public static void main_testing(String[] args) throws IOException {
        /*
        // Test on toy.net network
        Graph graph = Graphology.pajek("/home/luke/Documents/fax/magistrska/intermediacy/nets/toy.net");
        Graph intermediate = Graphology.induced(graph, Graphology.intermediate(graph, graph.getNode(1), graph.getNode(5)));
        System.out.println(intermediate.getLabels().length);
        */

        Graph graph = Graphology.pajek("/home/luke/Documents/fax/magistrska/test_env/data/arxiv_network.net");

        /*
        // Find the biggest source-target network from 40398 node
        // HashMap<String, String> attribs = graph.getNodeAttributes(graph.getNode(40398));
        // System.out.println(attribs.get("title"));

        int source = 40398;
        int[] targets = {4, 9, 58, 70, 114, 140, 182, 291, 292, 293, 313, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 366, 367, 377, 378, 382, 392, 413, 417, 418, 462, 521, 574, 594, 604, 680, 689, 690, 705, 706, 716, 721, 732, 743, 824, 885, 888, 939, 969, 992, 1002, 1010, 1011, 1030, 1048, 1059, 1060, 1061, 1087, 1105, 1107, 1108, 1141, 1196, 1197, 1199, 1216, 1224, 1230, 1253, 1270, 1280, 1304, 1305, 1308, 1327, 1527, 1540, 1606, 1608, 1629, 1724, 1792, 1814, 1815, 1919, 1949, 2031, 2041, 2101, 2163, 2188, 2204, 2228, 2230, 2232, 2233, 2264, 2306, 2315, 2318, 2413, 2509, 2511, 2517, 2524, 2525, 2539, 2633, 2637, 2673, 2707, 2724, 2756, 2813, 2817, 2887, 2892, 2967, 2971, 3116, 3223, 3224, 3225, 3226, 3227, 3228, 3229, 3230, 3231, 3264, 3338, 3342, 3345, 3361, 3385, 3404, 3406, 3407, 3496, 3509, 3513, 3651, 3694, 3695, 3697, 3781, 3856, 3893, 3894, 3995, 4045, 4046, 4143, 4145, 4150, 4151, 4153, 4161, 4179, 4180, 4183, 4219, 4251, 4254, 4260, 4262, 4271, 4320, 4396, 4417, 4433, 4434, 4435, 4436, 4437, 4441, 4447, 4469, 4565, 4569, 4572, 4608, 4725, 4803, 4936, 4937, 5002, 5004, 5019, 5025, 5052, 5074, 5075, 5076, 5077, 5096, 5097, 5099, 5100, 5101, 5102, 5103, 5104, 5153, 5180, 5181, 5182, 5185, 5246, 5263, 5264, 5289, 5298, 5321, 5362, 5403, 5510, 5540, 5583, 5585, 5593, 5594, 5602, 5603, 5686, 5700, 5758, 5759, 5760, 5849, 5850, 5851, 5877, 5894, 5910, 5993, 6034, 6115, 6117, 6118, 6143, 6152, 6167, 6274, 6275, 6285, 6313, 6314, 6315, 6367, 6368, 6455, 6526, 6538, 6539, 6540, 6541, 6542, 6543, 6544, 6545, 6546, 6547, 6548, 6549, 6550, 6552, 6645, 6646, 6687, 6688, 6689, 6745, 6748, 6752, 6755, 6830, 6831, 6832, 6833, 6834, 6836, 6897, 6931, 6981, 6982, 6983, 6984, 6985, 6986, 6988, 7005, 7007, 7008, 7086, 7120, 7138, 7146, 7200, 7244, 7270, 7292, 7333, 7354, 7355, 7374, 7375, 7376, 7478, 7539, 7632, 7679, 7680, 7720, 7721, 7747, 7749, 7751, 7813, 7814, 7819, 7854, 7925, 7927, 7928, 7932, 8024, 8048, 8049, 8151, 8184, 8200, 8240, 8312, 8315, 8488, 8499, 8646, 8648, 8650, 8653, 8676, 8680, 8744, 8764, 8765, 8766, 8767, 8830, 8831, 8832, 8833, 8925, 8926, 8942, 8944, 9057, 9058, 9063, 9131, 9136, 9138, 9139, 9140, 9141, 9172, 9188, 9193, 9264, 9350, 9381, 9382, 9397, 9406, 9445, 9476, 9504, 9606, 9637, 9638, 9678, 9756, 9769, 9782, 9783, 9812, 9844, 9895, 9925, 9926, 9927, 10030, 10058, 10059, 10079, 10084, 10085, 10116, 10158, 10160, 10199, 10206, 10230, 10240, 10242, 10250, 10253, 10254, 10256, 10265, 10266, 10268, 10271, 10276, 10299, 10300, 10301, 10324, 10367, 10368, 10373, 10378, 10392, 10393, 10447, 10448, 10484, 10485, 10489, 10498, 10499, 10511, 10599, 10602, 10603, 10623, 10712, 10725, 10742, 10744, 10776, 10833, 10842, 10880, 10935, 10979, 10981, 11033, 11121, 11122, 11169, 11174, 11190, 11235, 11236, 11250, 11255, 11305, 11317, 11382, 11412, 11455, 11489, 11514, 11515, 11516, 11517, 11518, 11519, 11577, 11599, 11661, 11662, 11663, 11703, 11716, 11717, 11718, 11795, 11864, 11895, 11997, 12050, 12051, 12115, 12201, 12203, 12212, 12214, 12251, 12290, 12367, 12369, 12435, 12558, 12614, 12650, 12720, 12754, 12755, 12756, 12759, 12795, 12825, 12826, 12827, 12828, 12829, 12897, 13002, 13007, 13023, 13078, 13079, 13107, 13172, 13173, 13187, 13247, 13248, 13249, 13251, 13269, 13270, 13282, 13283, 13301, 13303, 13326, 13327, 13332, 13333, 13355, 13474, 13590, 13597, 13599, 13695, 13712, 13839, 13840, 13914, 13915, 13933, 14031, 14188, 14189, 14262, 14264, 14287, 14438, 14482, 14483, 14496, 14508, 14511, 14512, 14533, 14534, 14535, 14553, 14610, 14623, 14770, 14790, 14791, 14797, 14805, 14843, 14883, 14887, 14912, 14913, 14915, 14916, 14997, 15003, 15035, 15079, 15080, 15182, 15234, 15284, 15444, 15511, 15512, 15514, 15558, 15721, 15725, 15734, 15748, 15774, 15796, 15862, 15929, 15939, 15940, 16018, 16134, 16135, 16219, 16271, 16344, 16370, 16406, 16417, 16443, 16449, 16457, 16461, 16580, 16608, 16727, 16768, 16792, 16793, 16794, 16802, 16884, 16885, 16918, 16981, 17008, 17091, 17277, 17294, 17322, 17346, 17373, 17430, 17451, 17452, 17540, 17604, 17606, 17607, 17636, 17744, 17745, 17765, 17813, 17814, 17887, 17959, 17983, 17986, 17987, 18004, 18032, 18033, 18046, 18047, 18049, 18074, 18158, 18159, 18194, 18213, 18221, 18394, 18481, 18513, 18522, 18523, 18582, 18790, 18826, 18926, 18964, 18965, 19029, 19030, 19031, 19090, 19144, 19145, 19175, 19326, 19357, 19535, 19789, 19810, 19978, 19980, 20072, 20129, 20130, 20285, 20286, 20362, 20418, 20482, 20500, 20501, 20502, 20503, 20504, 20505, 20506, 20548, 20593, 20594, 20595, 20606, 20610, 20695, 20703, 20775, 20889, 21051, 21151, 21152, 21153, 21154, 21155, 21178, 21180, 21273, 21324, 21349, 21407, 21410, 21431, 21608, 21747, 21995, 22041, 22056, 22057, 22169, 22223, 22224, 22455, 22456, 22457, 22458, 22493, 22662, 22694, 22715, 23201, 23234, 23299, 23321, 23334, 23371, 23445, 23446, 23447, 23448, 23449, 23450, 23464, 23574, 23644, 23645, 23681, 24026, 24040, 24098, 24101, 24203, 24531, 24905, 24906, 24907, 24908, 24909, 24910, 25020, 25021, 25022, 25283, 25417, 25453, 25454, 25455, 25624, 25625, 25626, 25628, 25725, 25780, 25791, 25862, 25863, 25954, 26052, 26053, 26054, 26209, 26210, 26348, 26349, 26429, 26636, 26652, 26654, 26681, 26683, 26752, 26781, 26783, 26793, 26894, 27032, 27033, 27180, 27296, 27308, 27356, 27397, 27398, 27469, 27515, 27531, 27532, 27974, 28080, 28081, 28082, 28235, 28287, 28295, 28296, 28297, 28400, 28401, 28415, 28420, 28606, 28664, 28683, 28779, 28784, 28792, 28815, 28816, 28834, 28958, 28970, 28971, 29232, 29233, 29277, 29278, 29312, 29313, 29367, 29421, 29472, 29613, 29637, 29643, 29644, 29736, 29823, 29932, 29942, 29963, 30362, 30363, 30427, 30458, 30459, 30608, 30653, 30792, 30863, 30929, 30930, 30931, 30932, 31031, 31072, 31073, 31076, 31179, 31180, 31250, 31965, 32001, 32216, 32247, 32317, 32336, 32353, 32405, 32406, 32659, 32771, 32796, 32842, 33238, 33307, 33351, 33372, 33565, 33655, 33722, 33861, 33906, 33947, 33948, 33960, 33963, 33997, 34069, 34211, 34251, 34316, 34384, 34468, 34543, 34688, 34791, 34798, 34799, 34885, 34955, 35037, 35077, 35082, 35083, 35084, 35085, 35163, 35210, 35288, 35322, 35397, 35416, 35538, 35539, 35540, 35678, 35769, 35897, 36027, 36077, 36123, 36124, 36179, 36180, 36353, 36634, 37038, 37039, 37280, 37461, 37462, 37670, 38840, 38917, 39482, 40398, 40399, 43562, 44113, 44241, 46023, 47778, 49010, 51525, 53525, 55604, 56197, 57411, 57569, 58753, 58773, 61245, 61600, 61601, 64557, 68816, 68869, 70866, 74628, 79494, 80553, 84898, 87409, 91110, 91738, 94798, 102925, 110291, 113702, 124923, 124924, 125034, 125045, 125091, 125092, 125191, 125251, 125367, 125390, 125409, 125521, 125724, 125834, 126005, 126042, 126043, 126059, 126067, 126079, 126095, 126163, 126314, 126336, 126442, 126509, 126603, 126747, 126757, 127043, 127073, 127074, 127173, 127282, 127283, 127284, 127398, 127568, 127604, 127643, 127737, 127892, 128018, 128487, 128567, 128659, 128662, 128663, 128914, 128915, 129065, 129066, 129580, 129677, 129678, 129685, 129737, 129900, 130171, 130939, 130940, 131307, 131477, 131678, 131707, 132364, 132713, 132726, 132901, 132974, 132975, 132997, 133218, 133285, 133370, 133445, 133628, 133685, 136039, 136340, 136990, 137036};
        for (int target : targets) {
            Graph intermediate = Graphology.induced(graph, Graphology.intermediate(graph, graph.getNode(source), graph.getNode(target)));
            System.out.printf("node: %d > %d\n", target, intermediate.getN());
        }
        */


        /*
        int source = 40398; // which type of citation analysis generates the most accurate taxonomy of scientific and technical knowledge
        int target = 332; // measuring contextual citation impact of scientific journals
        */

        /*
        int source = 40688; // role based label propagation algorithm for community detection
        int target = 29823; // community detection in complex networks using agents
         */

        /*
        int source = 100709; // from louvain to leiden guaranteeing well connected communities
        //int target = 1527; // community structure in large networks natural cluster sizes and the absence of large well defined clusters
        //int target = 9136; // generalized louvain method for community detection in large networks
        //int target = 2525; // a classification for community discovery methods in complex networks
        int target = 19535; // community detection using a neighborhood strength driven label propagation algorithm
        */

        int source = 40688; //role based label propagation algorithm for community detection
        int target = 9136; // generalized louvain method for community detection in large networks

        int samples = 1000;
        Double set_x = 0.7;
        Double alpha = 1.0; //0.00000001;

        Graph intermediate = Graphology.induced(graph, Graphology.intermediate(graph, graph.getNode(source), graph.getNode(target)));
        HashMap<String, Double> weights = prepare_weights(intermediate, set_x);

        List<Integer> nodes = new ArrayList<Integer>();
        for (int i = 0; i < intermediate.getN(); i++)
            if (intermediate.getLabel(i) != source && intermediate.getLabel(i) != target)
                nodes.add(i);

        //intermediacy_extended(Graph graph, int source, int target, int samples, HashMap<String, Double> weights, int method, Double alpha) {
        double[] intermediacies = Graphology.intermediacy_extended(intermediate, intermediate.getNode(source), intermediate.getNode(target), samples, weights, 1, alpha);

        print(intermediate, nodes, intermediacies, source, target, samples);

    }

    public static void run_test(Graph graph, int source, int target, int method, double set_x, double alpha) {
        int samples = 1000000;

        String method_str = "";
        switch (method) {
            case 1:
                method_str = "f(x) = x^alpha";
                break;
            case 2:
                method_str = "f(x) = alpha^x";
                break;
            case 3:
                method_str = "f(x) = x/(x + alpha)";
                break;
        }
        System.out.println("\n------------------------------------------------------\n");
        System.out.printf("Edge weight (x): %f\n", set_x);
        System.out.printf("Alpha: %f\n", alpha);
        System.out.println("Method: " + method_str);
        System.out.printf("Samples: %d\n", samples);


        Graph intermediate = Graphology.induced(graph, Graphology.intermediate(graph, graph.getNode(source), graph.getNode(target)));
        HashMap<String, Double> weights = prepare_weights(intermediate, set_x);

        List<Integer> nodes = new ArrayList<Integer>();
        for (int i = 0; i < intermediate.getN(); i++)
            if (intermediate.getLabel(i) != source && intermediate.getLabel(i) != target)
                nodes.add(i);

        double[] intermediacies = Graphology.intermediacy_extended(intermediate, intermediate.getNode(source), intermediate.getNode(target), samples, weights, method, alpha);

        print(intermediate, nodes, intermediacies, source, target, samples);
    }

    public static void analysis_1(Graph graph) {
        int source = 40398; // which type of citation analysis generates the most accurate taxonomy of scientific and technical knowledge
        //int source = 40688; //role based label propagation algorithm for community detection
        int target = 19535; // community detection using a neighborhood strength driven label propagation algorithm

        int method = 2;
        Double set_x = 5.0;
        Double alpha = 0.01; //0.00000001;
        run_test(graph, source, target, method, set_x, alpha);

        alpha = 0.7; //0.00000001;
        run_test(graph, source, target, method, set_x, alpha);

        alpha = 0.85; //0.00000001;
        run_test(graph, source, target, method, set_x, alpha);

        alpha = 0.9999; //0.00000001;
        run_test(graph, source, target, method, set_x, alpha);

        method = 3;
        set_x = 500.0;
        alpha = 0.0; //0.00000001;
        run_test(graph, source, target, method, set_x, alpha);

        set_x = 500.0;
        alpha = 10.0; //0.00000001;
        run_test(graph, source, target, method, set_x, alpha);

        alpha = 500.0; //0.00000001;
        run_test(graph, source, target, method, set_x, alpha);

        alpha = 750.0; //0.00000001;
        run_test(graph, source, target, method, set_x, alpha);

        alpha = 1000.0; //0.00000001;
        run_test(graph, source, target, method, set_x, alpha);
    }

    public static double[] prepare4corr(List<Integer> nodes, double[] intermediacy) {
        Collections.sort(nodes, new Comparator<Integer>() {
            @Override
            public int compare(Integer first, Integer second) {
                if (intermediacy[first] == intermediacy[second])
                    return new Integer(first).compareTo(second);

                return -new Double(intermediacy[first]).compareTo(intermediacy[second]);
            }
        });

        double[] out = new double[25];
        int i = 0;
        for (Integer node: nodes) {
            out[i] = (double) node;
            i++;
            if (i >= 25) {
                break;
            }
        }
        return out;
    }

    public static boolean check_zeroes(double[] intermediacy) {
        for (double val: intermediacy) {
            if (val > 0) {
                return true;
            }
        }
        return false;
    }

    public static void analysis_2(Graph graph) {
        int source = 40398; // which type of citation analysis generates the most accurate taxonomy of scientific and technical knowledge
        int target = 19535; // community detection using a neighborhood strength driven label propagation algorithm
        int samples = 1000000;
        int method = 2;
        Double set_x = 2.0;

        Graph intermediate = Graphology.induced(graph, Graphology.intermediate(graph, graph.getNode(source), graph.getNode(target)));
        HashMap<String, Double> weights = prepare_weights(intermediate, set_x);

        List<Integer> nodes = new ArrayList<Integer>();
        for (int i = 0; i < intermediate.getN(); i++) {
            if (intermediate.getLabel(i) != source && intermediate.getLabel(i) != target) {
                nodes.add(i);
            }
        }

        double[][] intermediacies = new double[10][];
        //double results[][] = new double[10][nodes.size()];

        for (int i = 0; i < 10; i++) {
            Double alpha = (double) i / 10;
            intermediacies[i] = Graphology.intermediacy_extended(intermediate, intermediate.getNode(source), intermediate.getNode(target), samples, weights, method, alpha);
            //results[i] = prepare4corr(nodes, intermediacies[i]);
        }

        System.out.printf("\n\nMethod: f(x) = alpha^x\nEdge weight(x): %f\nSamples: %d\n", set_x , samples);
        for (int i = 0; i < 10; i++) {
            for (int j = i; j < 10; j++) {
                //double corr = new PearsonsCorrelation().correlation(results[i], results[j]);
                double corr = new SpearmansCorrelation().correlation(intermediacies[i], intermediacies[j]);
                System.out.printf("Alpha X,Y (non-zero): %f, %f (%b,%b) | Correlation: %f\n", (double)i/10, (double)j/10,check_zeroes(intermediacies[i]), check_zeroes(intermediacies[j]), corr);
            }
        }

        System.out.println("\n\nCorrelation matrix\n");

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                double corr = new SpearmansCorrelation().correlation(intermediacies[i], intermediacies[j]);
                System.out.printf("%f, ", corr);
            }
            System.out.println("");
        }

        // --------------------------------------------------------------------------
        set_x = 500.0;
        weights = prepare_weights(intermediate, set_x);
        method = 3;
        intermediacies = new double[20][];

        for (int i = 0; i < 20; i++) {
            Double alpha = (double) i * 100;
            intermediacies[i] = Graphology.intermediacy_extended(intermediate, intermediate.getNode(source), intermediate.getNode(target), samples, weights, method, alpha);
            //results[i] = prepare4corr(nodes, intermediacies[i]);
        }

        System.out.printf("\n\nMethod: f(x) = x/(x + alpha)\nEdge weight(x): %f\nSamples: %d\n", set_x, samples);
        for (int i = 0; i < 20; i++) {
            for (int j = i; j < 20; j++) {
                double corr = new SpearmansCorrelation().correlation(intermediacies[i], intermediacies[j]);
                System.out.printf("Alpha X,Y (non-zero): %f, %f (%b,%b) | Correlation: %f\n", (double)i*100, (double)j*100, check_zeroes(intermediacies[i]), check_zeroes(intermediacies[j]), corr);
            }
        }

        System.out.println("\n\nCorrelation matrix\n");

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                double corr = new SpearmansCorrelation().correlation(intermediacies[i], intermediacies[j]);
                System.out.printf("%f, ", corr);
            }
            System.out.println("");
        }
    }


    public static void main(String[] args) throws IOException {
        Graph graph = Graphology.pajek("/home/luke/Documents/fax/magistrska/test_env/data/arxiv_network.net");
        System.out.println("----------------------------------------------\n----------------- ANALYSIS 1 -----------------\n----------------------------------------------");
        analysis_1(graph);
        System.out.println("\n----------------------------------------------\n----------------- ANALYSIS 2 -----------------\n----------------------------------------------");
        //analysis_2(graph);
    }
}

