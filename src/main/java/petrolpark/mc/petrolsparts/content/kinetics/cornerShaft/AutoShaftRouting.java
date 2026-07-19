package petrolpark.mc.petrolsparts.content.kinetics.cornerShaft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.annotation.Nullable;

import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.math.BlockFace;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import petrolpark.mc.petrolsparts.PetrolsPartsBlocks;

public class AutoShaftRouting {

    /**
     * A* with heuristic to minimize bends
     * @param start
     * @param goal
     */
    public static final List<Pair<BlockPos, BlockState>> getPath(Level level, BlockFace start, BlockFace goal) {
        if (start.equals(goal) || start.equals(goal.getOpposite())) return Collections.emptyList();

        final PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparing(Node::f));
        final Map<BlockFace, Cost> bestCosts = new HashMap<>();

        final Cost startCost = new Cost(0, 0);

        frontier.add(
            new Node(
                start,
                startCost,
                startCost.add(heuristic(start, goal)),
                null
            )
        );

        bestCosts.put(start, startCost);

        while (!frontier.isEmpty()) {

            final Node current = frontier.poll();

            if (current.face().equals(goal)) return getBlockStatesForPath(current);

            for (final BlockFace next : getNeighbours(level, start, current.face())) {

                final Cost newCost = current.g().add(current.face().getFace() != next.getFace());
                final Cost oldCost = bestCosts.get(next);

                if (oldCost != null && oldCost.compareTo(newCost) <= 0) continue;

                bestCosts.put(next, newCost);

                final Cost f = newCost.add(heuristic(next, goal));

                frontier.add(new Node(next, newCost, f, current));
            };
        };

        return Collections.emptyList();
    };

    public static final List<BlockFace> getNeighbours(Level level, BlockFace start, BlockFace current) {
        final BlockPos nextPos = current.getConnectedPos();
        if (!level.getBlockState(nextPos).canBeReplaced() || nextPos.distSqr(start.getPos()) > 256) return Collections.emptyList();

        final List<BlockFace> neighbours = new ArrayList<>();

        for (final Direction direction : Iterate.directions) {
            if (direction == current.getFace().getOpposite()) continue;

            neighbours.add(new BlockFace(nextPos, direction));
        };

        return neighbours;
    };

    public static final Cost heuristic(BlockFace current, BlockFace goal) {
        return new Cost(0, goal.getPos().distManhattan(current.getPos()));
    };

    public static final List<Pair<BlockPos, BlockState>> getBlockStatesForPath(Node goal) {
        final List<BlockFace> path = new ArrayList<>();
        for (Node node = goal; node != null; node = node.parent()) {
            path.add(node.face());
        };
        Collections.reverse(path);
        if (path.size() < 2) return Collections.emptyList();
        final List<Pair<BlockPos, BlockState>> states = new ArrayList<>(path.size() - 1);

        for (int i = 1; i < path.size(); i++) {
            final BlockFace face = path.get(i);
            states.add(Pair.of(face.getPos(), PetrolsPartsBlocks.CORNER_SHAFT.get().getBlockstateConnectingDirections(path.get(i - 1).getFace().getOpposite(), face.getFace())));
        };

        return states;
    };
    
    record Node(BlockFace face, Cost g, Cost f, @Nullable Node parent) {};

    record Cost(int bends, int length) implements Comparable<Cost> {

        @Override
        public int compareTo(Cost o) {
            final int c = Integer.compare(bends(), o.bends());
            return c == 0 ? Integer.compare(length(), o.length()) : c;
        };

        public Cost add(boolean bend) {
            return new Cost(bends() + (bend ? 1 : 0), length() + 1);
        };

        public Cost add(Cost o) {
            return new Cost(bends() + o.bends(), length() + o.length());
        };

    };
};
