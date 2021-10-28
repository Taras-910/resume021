package ua.training.top.aggregator.strategy;

import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;

import java.io.IOException;
import java.util.List;

public interface Strategy {
    List<ResumeTo> getResumes (Freshen doubleString) throws IOException;
}
