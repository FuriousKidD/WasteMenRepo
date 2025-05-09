import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Waste Collection Simulation Program
 * Simulates robots collecting and sorting waste based on color recognition
 */
public class WasteCollectionSimulation {
    // ==================== CONSTANTS ====================
    static final int SMALL = 20, MEDIUM = 25, LARGE = 29;
    static final char WALL = '#';
    static final char WALKABLE = '.';
    static final char ROBOT_EMPTY = 'R';
    static final char ROBOT_CARRYING = 'O';
    static final char UNIDENTIFIED_WASTE = '?';
    static final char PLASTIC_WASTE = 's';
    static final char PAPER_WASTE = 'p';
    static final char METAL_WASTE = 'm';
    static final char GLASS_WASTE = 'g';
    static final char PLASTIC_BIN = 'S';
    static final char PAPER_BIN = 'P';
    static final char METAL_BIN = 'M';
    static final char GLASS_BIN = 'G';
    static final char EXPLORED_AREA = '.';
    static final char FIELD_OF_VIEW = ' ';
    
 // ANSI color codes
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";  // Green color
    
    static final String WASTE_IMAGE_PATH = "images/";
    static final String BIN_IMAGE_PATH = "bin_images/";

    // ==================== SIMULATION STATE ====================
    private char[][] map;
    private char[][] displayMap;
    private List<Robot> robots = new ArrayList<>();
    private List<Waste> wastes = new ArrayList<>();
    private List<Bin> bins = new ArrayList<>();
    private int fieldOfView;
    private int rows, cols;
    private boolean mapFullyExplored = false;
    private boolean isPaused = true;
    private boolean waitingForUser = false;
    private Scanner scanner = new Scanner(System.in);

    // ==================== MAIN METHOD ====================
    public static void main(String[] args) {
        System.out.println("Waste Sorting Simulation - Console Version");
        new WasteCollectionSimulation(LARGE, 1, 2);
    }

    // ==================== INITIALIZATION METHODS ====================

    /**
     * 1. Simulation constructor - Sets up the initial state
     */
    public WasteCollectionSimulation(int size, int numRobots, int fieldOfView) {
        this.rows = size;
        this.cols = size;
        this.fieldOfView = fieldOfView;

        generateMap();          // 2. Create the game map
        spawnRobots(numRobots); // 3. Place robots on map

        System.out.println("Simulation Initialized - Map Size: " + rows + "x" + cols);
        System.out.println("Press Enter to start...");
        scanner.nextLine();
        
        startSimulation();      // 4. Begin main simulation loop
    }

    /**
     * 2. Generate the game map with walls, bins and wastes
     */
    private void generateMap() {
        map = new char[rows][cols];
        Random rand = new Random();
        
        // Initialize all cells as walkable
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                map[r][c] = WALKABLE;
            }
        }

        // Add border walls
        for (int i = 0; i < rows; i++) {
            map[i][0] = map[i][cols - 1] = WALL;
        }
        for (int i = 0; i < cols; i++) {
            map[0][i] = map[rows - 1][i] = WALL;
        }

        // Add random interior walls
        for (int i = 0; i < rows * cols / 6; i++) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (map[r][c] == WALKABLE) {
                map[r][c] = WALL;
            }
        }

        // Add bins for each waste type
        for (WasteType type : WasteType.values()) {
            File typeFolder = new File(BIN_IMAGE_PATH + type.toString().toLowerCase());
            File[] binImages = typeFolder.listFiles();
            
            for (int i = 0; i < 2; i++) {
                int r, c;
                do {
                    r = rand.nextInt(rows);
                    c = rand.nextInt(cols);
                } while (map[r][c] != WALKABLE);
                
                char binChar = switch (type) {
                    case PLASTIC -> PLASTIC_BIN;
                    case PAPER -> PAPER_BIN;
                    case METAL -> METAL_BIN;
                    case GLASS -> GLASS_BIN;
                };
                
                map[r][c] = binChar;
                if (binImages != null && binImages.length > 0) {
                    bins.add(new Bin(r, c, type, binImages[rand.nextInt(binImages.length)]));
                }
            }
        }

        // Add waste items
        for (WasteType type : WasteType.values()) {
            File typeFolder = new File(WASTE_IMAGE_PATH + type.toString().toLowerCase());
            File[] wasteImages = typeFolder.listFiles();
            
            for (int i = 0; i < 3; i++) {
                int r, c;
                do {
                    r = rand.nextInt(rows);
                    c = rand.nextInt(cols);
                } while (map[r][c] != WALKABLE);
                
                map[r][c] = UNIDENTIFIED_WASTE;
                if (wasteImages != null && wasteImages.length > 0) {
                    wastes.add(new Waste(r, c, type, wasteImages[rand.nextInt(wasteImages.length)]));
                }
            }
        }
    }

    /**
     * 3. Place robots at random walkable positions
     */
    private void spawnRobots(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int r, c;
            do {
                r = rand.nextInt(rows);
                c = rand.nextInt(cols);
            } while (map[r][c] != WALKABLE);
            robots.add(new Robot(r, c));
        }
    }

    // ==================== SIMULATION LOOP METHODS ====================

    /**
     * 4. Main simulation loop
     */
    private void startSimulation() {
        isPaused = false;
        while (!mapFullyExplored) {
            if (!isPaused && !waitingForUser) {
                updateSimulation();    // 5. Update all entities
                updateDisplayMap();    // 6. Refresh visual representation
                printMap();            // 7. Draw current state
                
                try {
                    Thread.sleep(500); // Pause for visibility
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            mapFullyExplored = isMapFullyExplored(); // 8. Check completion
            if (mapFullyExplored) {
                System.out.println("\nMAP FULLY EXPLORED - SIMULATION COMPLETE!");
                break;
            }
        }
    }

    /**
     * 5. Update all simulation entities
     */
    private void updateSimulation() {
        for (Robot robot : robots) {
            robot.act(); // 5.1 Process each robot's turn
        }
    }

    /**
     * 6. Update the display map representation
     */
    private void updateDisplayMap() {
        displayMap = new char[rows][cols];
        
        // Copy base map
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                displayMap[r][c] = map[r][c];
            }
        }

        // Update waste display status
        for (Waste waste : wastes) {
            if (waste.identified) {
                char wasteChar = switch (waste.type) {
                    case PLASTIC -> PLASTIC_WASTE;
                    case PAPER -> PAPER_WASTE;
                    case METAL -> METAL_WASTE;
                    case GLASS -> GLASS_WASTE;
                };
                displayMap[waste.row][waste.col] = wasteChar;
            } else {
                displayMap[waste.row][waste.col] = UNIDENTIFIED_WASTE;
            }
        }

        // Mark explored areas
        for (Robot robot : robots) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (robot.exploredMap[r][c] && displayMap[r][c] == WALKABLE) {
                        displayMap[r][c] = EXPLORED_AREA;
                    }
                }
            }
        }

        // Mark field of view
        for (Robot robot : robots) {
            int minRow = Math.max(0, robot.row - fieldOfView);
            int maxRow = Math.min(rows - 1, robot.row + fieldOfView);
            int minCol = Math.max(0, robot.col - fieldOfView);
            int maxCol = Math.min(cols - 1, robot.col + fieldOfView);

            for (int r = minRow; r <= maxRow; r++) {
                for (int c = minCol; c <= maxCol; c++) {
                    if (robot.hasLineOfSight(robot.row, robot.col, r, c) && 
                        displayMap[r][c] == WALKABLE) {
                        displayMap[r][c] = FIELD_OF_VIEW;
                    }
                }
            }
        }
    }

    /**
     * 7. Print the current map state to console
     */
    private void printMap() {
        System.out.print("\033[H\033[2J"); // Clear console
        System.out.flush();

        System.out.println("WASTE SORTING SIMULATION - CURRENT STATE");
        System.out.println("Legend: #=Wall .=Walkable R=Robot O=CarryingRobot");
        System.out.println("?=Unknown s=Plastic p=Paper m=Metal g=Glass");
        System.out.println("S=PlasticBin P=PaperBin M=MetalBin G=GlassBin");
        System.out.println("x=Explored ' '=FieldOfView\n");

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Check for robots first
                boolean hasRobot = false;
                char robotChar = ROBOT_EMPTY;
                for (Robot robot : robots) {
                    if (robot.row == r && robot.col == c) {
                        hasRobot = true;
                        robotChar = robot.carrying != null ? ROBOT_CARRYING : ROBOT_EMPTY;
                        break;
                    }
                }

                System.out.print(hasRobot ? robotChar : displayMap[r][c]);
                System.out.print(" ");
            }
            System.out.println();
        }
        
        System.out.println();
    }

    /**
     * 8. Check if map is fully explored
     */
    private boolean isMapFullyExplored() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] != WALL) {
                    boolean exploredByAny = false;
                    for (Robot robot : robots) {
                        if (robot.exploredMap[r][c]) {
                            exploredByAny = true;
                            break;
                        }
                    }
                    if (!exploredByAny) return false;
                }
            }
        }
        return true;
    }

    // ==================== ROBOT CLASS ====================
    class Robot {
        int row, col;
        Waste carrying = null;
        Bin targetBin = null;
        List<Point> path = new ArrayList<>();
        List<Point> knownWastes = new ArrayList<>();
        List<Bin> knownBins = new ArrayList<>();
        boolean[][] exploredMap;
        Point explorationTarget;

        /**
         * 9. Robot initialization
         */
        public Robot(int r, int c) {
            this.row = r;
            this.col = c;
            this.exploredMap = new boolean[rows][cols];
            exploreCurrentPosition(); // 9.1 Initial exploration
        }

        /**
         * 9.1 Explore current position and surroundings
         */
        private void exploreCurrentPosition() {
            exploredMap[row][col] = true;

            int minRow = Math.max(0, row - fieldOfView);
            int maxRow = Math.min(rows - 1, row + fieldOfView);
            int minCol = Math.max(0, col - fieldOfView);
            int maxCol = Math.min(cols - 1, col + fieldOfView);

            for (int r = minRow; r <= maxRow; r++) {
                for (int c = minCol; c <= maxCol; c++) {
                    if (hasLineOfSight(row, col, r, c)) {
                        exploredMap[r][c] = true;
                        
                        // Discover wastes
                        if (map[r][c] == UNIDENTIFIED_WASTE) {
                            Point wasteLoc = new Point(r, c);
                            if (!knownWastes.contains(wasteLoc)) {
                                knownWastes.add(wasteLoc);
                            }
                        }
                        
                        // Discover bins
                        char cell = map[r][c];
                        if (cell == PLASTIC_BIN || cell == PAPER_BIN || 
                            cell == METAL_BIN || cell == GLASS_BIN) {
                            Bin bin = findBinAt(r, c);
                            if (bin != null && !knownBins.contains(bin)) {
                                knownBins.add(bin);
                            }
                        }
                    }
                }
            }
        }

        /**
         * 10. Main robot action processing
         */
        public void act() {
            if (waitingForUser) return;

            exploreCurrentPosition(); // 10.1 Continue exploring
            identifyWastes();        // 10.2 Detect nearby wastes
            handleMovement();       // 10.3 Move or perform actions
        }

        /**
         * 10.1 Identify wastes in line of sight
         */
        private void identifyWastes() {
            for (Waste waste : wastes) {
                if (hasLineOfSight(row, col, waste.row, waste.col)) {
                    waste.identified = true;
                    Point wasteLoc = new Point(waste.row, waste.col);
                    if (!knownWastes.contains(wasteLoc)) {
                        knownWastes.add(wasteLoc);
                    }
                }
            }
        }

        /**
         * 10.2 Handle robot movement and actions
         */
        private void handleMovement() {
            if (carrying != null) {
                handleWasteDisposal(); // 10.2.1 Dispose of carried waste
            } else {
                findAndCollectWaste(); // 10.2.2 Find new waste to collect
            }
        }

        /**
         * 10.2.1 Handle waste disposal process
         */
        private void handleWasteDisposal() {
            if (targetBin != null) {
                if (path.isEmpty()) {
                    path = findPathToBin(targetBin); // 10.2.1.1 Find path to bin
                }

                if (!path.isEmpty()) {
                    moveAlongPath(); // 10.2.1.2 Follow path to bin
                }

                if (row == targetBin.row && col == targetBin.col) {
                    showDisposalDialog(carrying, targetBin); // 10.2.1.3 Show disposal info
                    carrying = null;
                    targetBin = null;
                }
            } else {
                targetBin = findNearestMatchingBin(carrying.type); // 10.2.1.4 Find correct bin
                if (targetBin != null) {
                    path = findPathToBin(targetBin);
                }
            }
        }

        /**
         * 10.2.2 Find and collect waste
         */
        private void findAndCollectWaste() {
            // Try to pick up waste at current position
            for (Waste waste : wastes) {
                if (waste.row == row && waste.col == col) {
                    carrying = waste;
                    wastes.remove(waste);
                    map[row][col] = WALKABLE;
                    knownWastes.remove(new Point(row, col));

                    WasteType classifiedType = classifyWaste(waste.imageFile); // 11. Classify waste
                    carrying.type = classifiedType;

                    showClassificationDialog(waste); // Show classification info
                    return;
                }
            }

            // Move toward nearest known waste
            if (!knownWastes.isEmpty()) {
                Point nearestWaste = findNearestWaste(); // 10.2.2.1 Find closest waste
                if (nearestWaste != null) {
                    path = findPath(new Point(row, col), nearestWaste);
                    if (!path.isEmpty()) {
                        moveAlongPath(); // 10.2.2.2 Move toward waste
                    }
                }
            } 
            // Explore if no wastes known
            else {
                if (explorationTarget == null || (row == explorationTarget.x && col == explorationTarget.y)) {
                    explorationTarget = findNearestUnexplored(); // 10.2.2.3 Find new area to explore
                }

                if (explorationTarget != null) {
                    path = findPath(new Point(row, col), explorationTarget);
                    if (!path.isEmpty()) {
                        moveAlongPath(); // 10.2.2.4 Move to exploration target
                    }
                } else {
                    randomMove(); // 10.2.2.5 Random movement if no targets
                }
            }
        }

        /**
         * 11. Classify waste by analyzing image color
         */
        private WasteType classifyWaste(File wasteImage) {
            try {
                BufferedImage image = ImageIO.read(wasteImage);
                if (image == null) {
                    System.err.println("Could not read waste image: " + wasteImage.getName());
                    return WasteType.PLASTIC;
                }

                Color dominantColor = getDominantColor(image); // 11.1 Get main color
                
                // 11.2 Determine type from color
                if (isRed(dominantColor)) return WasteType.METAL;
                if (isBlue(dominantColor)) return WasteType.PAPER;
                if (isYellow(dominantColor)) return WasteType.PLASTIC;
                if (isGreen(dominantColor)) return WasteType.GLASS;
                
                return WasteType.PLASTIC; // Default fallback
            } catch (IOException e) {
                System.err.println("Error reading waste image: " + wasteImage.getName());
                return WasteType.PLASTIC;
            }
        }

        // ==================== ROBOT HELPER METHODS ====================

        /**
         * 11.1 Get dominant color from image (simple center-pixel approach)
         */
        private Color getDominantColor(BufferedImage image) {
            int x = image.getWidth() / 2;
            int y = image.getHeight() / 2;
            return new Color(image.getRGB(x, y));
        }

        /**
         * 11.2 Color detection methods
         */
        private boolean isRed(Color color) {
            return color.getRed() > color.getGreen() + 50 && 
                   color.getRed() > color.getBlue() + 50;
        }

        private boolean isBlue(Color color) {
            return color.getBlue() > color.getRed() + 50 && 
                   color.getBlue() > color.getGreen() + 50;
        }

        private boolean isYellow(Color color) {
            return color.getRed() > 200 && 
                   color.getGreen() > 200 && 
                   color.getBlue() < 100;
        }

        private boolean isGreen(Color color) {
            return color.getGreen() > color.getRed() + 50 && 
                   color.getGreen() > color.getBlue() + 50;
        }

        /**
         * 10.2.1.1 Find path to target bin
         */
        private List<Point> findPathToBin(Bin bin) {
            return findPath(new Point(row, col), new Point(bin.row, bin.col));
        }

        /**
         * 10.2.2.1 Find nearest known waste
         */
        private Point findNearestWaste() {
            Point nearest = null;
            double minDistance = Double.MAX_VALUE;

            Iterator<Point> iterator = knownWastes.iterator();
            while (iterator.hasNext()) {
                Point wasteLoc = iterator.next();
                boolean wasteExists = false;

                for (Waste w : wastes) {
                    if (w.row == wasteLoc.x && w.col == wasteLoc.y) {
                        wasteExists = true;
                        break;
                    }
                }

                if (!wasteExists) {
                    iterator.remove();
                    continue;
                }

                double distance = Math.sqrt(Math.pow(wasteLoc.x - row, 2) + Math.pow(wasteLoc.y - col, 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = wasteLoc;
                }
            }

            return nearest;
        }

        /**
         * 10.2.2.3 Find nearest unexplored area
         */
        private Point findNearestUnexplored() {
            Queue<Point> queue = new LinkedList<>();
            boolean[][] visited = new boolean[rows][cols];
            Map<Point, Point> parent = new HashMap<>();

            queue.add(new Point(row, col));
            visited[row][col] = true;
            parent.put(new Point(row, col), null);

            while (!queue.isEmpty()) {
                Point current = queue.poll();

                if (!exploredMap[current.x][current.y]) {
                    while (parent.get(current) != null && !parent.get(current).equals(new Point(row, col))) {
                        current = parent.get(current);
                    }
                    return current;
                }

                for (int[] dir : new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }) {
                    int nr = current.x + dir[0];
                    int nc = current.y + dir[1];
                    if (nr >= 0 && nc >= 0 && nr < rows && nc < cols && map[nr][nc] != WALL && !visited[nr][nc]) {
                        Point neighbor = new Point(nr, nc);
                        queue.add(neighbor);
                        visited[nr][nc] = true;
                        parent.put(neighbor, current);
                    }
                }
            }
            return null;
        }

        /**
         * Pathfinding between two points
         */
        private List<Point> findPath(Point start, Point goal) {
            Queue<Point> queue = new LinkedList<>();
            Map<Point, Point> parent = new HashMap<>();

            queue.add(start);
            parent.put(start, null);

            while (!queue.isEmpty()) {
                Point current = queue.poll();
                if (current.equals(goal)) break;

                for (int[] dir : new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }) {
                    int nr = current.x + dir[0];
                    int nc = current.y + dir[1];
                    Point neighbor = new Point(nr, nc);

                    if (nr >= 0 && nc >= 0 && nr < rows && nc < cols && 
                        map[nr][nc] != WALL && !parent.containsKey(neighbor)) {
                        parent.put(neighbor, current);
                        queue.add(neighbor);
                    }
                }
            }

            List<Point> path = new ArrayList<>();
            Point current = goal;
            while (current != null && !current.equals(start)) {
                path.add(0, current);
                current = parent.get(current);
            }
            return path;
        }

        /**
         * Move along calculated path
         */
        private void moveAlongPath() {
            Point next = path.get(0);
            if (map[next.x][next.y] != WALL) {
                row = next.x;
                col = next.y;
                path.remove(0);
            }
        }

        /**
         * Random movement when no targets
         */
        private void randomMove() {
            int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
            Collections.shuffle(Arrays.asList(directions));

            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && 
                    map[newRow][newCol] != WALL) {
                    row = newRow;
                    col = newCol;
                    break;
                }
            }
        }

        /**
         * Check line of sight between two points
         */
        private boolean hasLineOfSight(int x0, int y0, int x1, int y1) {
            if (Math.abs(x1 - x0) > fieldOfView || Math.abs(y1 - y0) > fieldOfView) {
                return false;
            }

            int dx = Math.abs(x1 - x0);
            int dy = Math.abs(y1 - y0);
            int sx = x0 < x1 ? 1 : -1;
            int sy = y0 < y1 ? 1 : -1;
            int err = dx - dy;

            while (true) {
                if (map[x0][y0] == WALL) return false;
                if (x0 == x1 && y0 == y1) return true;

                int e2 = 2 * err;
                if (e2 > -dy) { err -= dy; x0 += sx; }
                if (e2 < dx) { err += dx; y0 += sy; }
            }
        }

        /**
         * Find bin at specific coordinates
         */
        private Bin findBinAt(int r, int c) {
            for (Bin bin : bins) {
                if (bin.row == r && bin.col == c) {
                    return bin;
                }
            }
            return null;
        }

        /**
         * Find nearest bin of matching type
         */
        private Bin findNearestMatchingBin(WasteType type) {
            Bin nearestBin = null;
            double minDistance = Double.MAX_VALUE;

            for (Bin bin : knownBins) {
                if (bin.type == type) {
                    double distance = Math.sqrt(Math.pow(bin.row - row, 2) + Math.pow(bin.col - col, 2));
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestBin = bin;
                    }
                }
            }

            if (nearestBin == null) {
                for (Bin bin : bins) {
                    if (bin.type == type) {
                        double distance = Math.sqrt(Math.pow(bin.row - row, 2) + Math.pow(bin.col - col, 2));
                        if (distance < minDistance) {
                            minDistance = distance;
                            nearestBin = bin;
                        }
                    }
                }
            }

            return nearestBin;
        }

        /**
         * Show waste classification dialog
         */
        private void showClassificationDialog(Waste waste) {
            waitingForUser = true;
            
            System.out.println("\n=== WASTE CLASSIFICATION ===");
            System.out.println("Robot at (" + row + "," + col + ") picked up:");
            System.out.println("Image: " + waste.imageFile.getName());
            System.out.println("Detected Color: " + getColorName(waste.imageFile));
            System.out.println("Classified as: " + waste.type);
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            
            waitingForUser = false;
        }

        /**
         * Get color name from image
         */
        private String getColorName(File imageFile) {
            try {
                BufferedImage image = ImageIO.read(imageFile);
                if (image == null) return "Unknown";
                
                Color color = getDominantColor(image);
                if (isRed(color)) return "Red (Metal)";
                if (isBlue(color)) return "Blue (Paper)";
                if (isYellow(color)) return "Yellow (Plastic)";
                if (isGreen(color)) return "Green (Glass)";
                return "Unknown";
            } catch (IOException e) {
                return "Unknown";
            }
        }

        /**
         * Show waste disposal dialog
         */
        private void showDisposalDialog(Waste waste, Bin bin) {
            waitingForUser = true;
            
            System.out.println("\n=== WASTE DISPOSAL ===");
            System.out.println("Robot at (" + row + "," + col + ") disposing:");
            System.out.println("Waste Image: " + waste.imageFile.getName());
            System.out.println("Waste Type: " + waste.type);
            System.out.println("Into Bin Image: " + bin.imageFile.getName());
            System.out.println("Bin Type: " + bin.type);
            
            if (waste.type == bin.type) {
                System.out.println("CORRECT DISPOSAL!");
            } else {
                System.out.println("INCORRECT DISPOSAL! Wrong bin type!");
            }
            
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            
            waitingForUser = false;
        }
    }

    // ==================== SUPPORTING CLASSES ====================

    /**
     * Waste types enumeration
     */
    enum WasteType {
        PLASTIC, PAPER, METAL, GLASS
    }

    /**
     * Waste class representing waste items
     */
    class Waste {
        int row, col;
        WasteType type;
        boolean identified = false;
        File imageFile;

        public Waste(int row, int col, WasteType type, File imageFile) {
            this.row = row;
            this.col = col;
            this.type = type;
            this.imageFile = imageFile;
        }
    }

    /**
     * Bin class representing recycling bins
     */
    class Bin {
        int row, col;
        WasteType type;
        File imageFile;

        public Bin(int r, int c, WasteType t, File imageFile) {
            row = r;
            col = c;
            type = t;
            this.imageFile = imageFile;
        }
    }

    /**
     * Point class for coordinates
     */
    class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Point point = (Point) obj;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}