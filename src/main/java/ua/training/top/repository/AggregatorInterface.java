package ua.training.top.repository;

import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;

import java.util.List;

public interface AggregatorInterface {
    List<ResumeTo> selectBy(Freshen freshen);
}
