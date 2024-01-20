package th.co.gosoft.rms.mm.pdm.pi.utils;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanPropertyRowMapperWithDefaultValue<T> extends BeanPropertyRowMapper<T> {

    public BeanPropertyRowMapperWithDefaultValue(Class<T> class_) {
        super(class_);
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        Class<?> requiredType = pd.getPropertyType();
        if (String.class.equals(requiredType)) {
            return StringUtils.convNullToEmptyStr(rs.getString(index));
        }       
        return super.getColumnValue(rs, index, pd);
    }
}