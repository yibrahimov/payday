package az.ibar.payday.ms.stock.mapper;

import az.ibar.payday.ms.stock.entity.StockEntity;
import az.ibar.payday.ms.stock.model.StockView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class StockMapper {

    public static final StockMapper INSTANCE = getMapper(StockMapper.class);

    @Mapping(source = "price", target = "price.value")
    @Mapping(source = "currency", target = "price.currency")

    public abstract StockView stockViewFromEntity(StockEntity stockEntity);
}
