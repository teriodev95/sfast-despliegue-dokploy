package tech.calaverita.reporterloanssql.persistence.mappers;

public interface IMapper<I, J> {
    public J mapOut(I out);

    public I mapIn(J in);
}
