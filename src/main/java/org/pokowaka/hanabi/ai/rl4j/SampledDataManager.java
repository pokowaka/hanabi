package org.pokowaka.hanabi.ai.rl4j;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.deeplearning4j.rl4j.util.DataManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Queue;
import java.util.Random;

/**
 * Resevoir sample the stat entry logs, so the web app doesn't crash due to much data.
 */
public class SampledDataManager extends DataManager {

    Queue<StatEntry> stats;
    int size;
    int sample = 0;
    Random rnd = new Random();
    final private ObjectMapper mapper = new ObjectMapper();

    public SampledDataManager() throws IOException {
        this(100);
    }

    public SampledDataManager(int sampleSize) throws IOException {
        super(true);
        this.size = sampleSize;
        stats = new CircularFifoQueue<>(sampleSize);
    }

    @Override
    public void appendStat(StatEntry statEntry) throws IOException {
        sample = sample + 1;
        if (rnd.nextInt(sample) < size) {
            stats.add(statEntry);
            Path statPath = Paths.get(getStat());
            String entry = "";
            for (StatEntry s : stats) {
                entry += mapper.writeValueAsString(statEntry) + "\n";
            }
            Files.write(statPath, entry.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        }
    }
}
