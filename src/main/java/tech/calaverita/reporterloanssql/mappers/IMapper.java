package tech.calaverita.reporterloanssql.mappers;

import java.util.ArrayList;

public interface IMapper<Model, DTO> {
    DTO mapOut(Model out);

    Model mapIn(DTO in);

    ArrayList<DTO> mapOuts(ArrayList<Model> outs);

    ArrayList<Model> mapIns(ArrayList<DTO> ins);
}
