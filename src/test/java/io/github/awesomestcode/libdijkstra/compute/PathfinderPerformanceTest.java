package io.github.awesomestcode.libdijkstra.compute;

import io.github.awesomestcode.libdijkstra.common.Grid;
import org.junit.jupiter.api.Test;

class PathfinderPerformanceTest {

    @Test
    void search() {
        Grid grid = new Grid(384,384);
        Pathfinder.Path path;
        for(int i = 0; i < 20; i++) {
            path = Pathfinder.search(0, 0, 5, 5, new Grid(384, 384));
            path = Pathfinder.search(0, 0, 100, 100, new Grid(384, 384));
            path = Pathfinder.search(0, 0, 200, 200, new Grid(384, 384));
            path = Pathfinder.search(0, 0, 300, 300, new Grid(384, 384));
            path = Pathfinder.search(0, 0, 383, 383, new Grid(384, 384));
        }

    }
}