package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import java.util.EnumMap;
import java.util.logging.Logger;

public class Immobilie
{

    private static final Logger Debug = Logger.getLogger(Immobilie.class.getName());


    //===========================================================================|| DATA ||===========================================================================================================


    public enum AttributeKey
    {
        ID("id", Integer.class),
        VERTICAL_ID("verticalId", Integer.class),
        AD_TYPE_ID("adTypeId", Integer.class),
        PRODUCT_ID("productId", Integer.class),

        DESCRIPTION("description", String.class),
        IMMO_TYPE("immoType", String.class),

        POSTCODE("postcode", Integer.class),
        DISTRICT("district", String.class),
        ADDRESS("address", String.class),

        PRICE("price", Double.class),
        PRICE_DISPLAY("priceDisplay", String.class);

        private final String key;
        private final Class<?> type;

        // Constructor
        AttributeKey(String key, Class<?> type)
        {
            this.key = key;
            this.type = type;
        }

        // Getter for the key
        public String getKey()
        {
            return key;
        }

        // Getter for the expected type
        public Class<?> getType()
        {
            return type;
        }
    }

    private final EnumMap<AttributeKey, Object> attributes = new EnumMap<>(AttributeKey.class);


    public Immobilie()
    {

    }

    //===========================================================================|| SIDE FUNCTIONS ||===========================================================================================================


    // Getters and Setters

    // Setter
    // Setter
    public <T> void setAttribute(AttributeKey key, String value)
    {
        try // Convert the string value to the target type
        {
            T convertedValue = convertToTargetType(key, value);
            attributes.put(key, convertedValue);
        }
        catch (Exception e) // Assign the default value if conversion fails
        {
            T defaultValue = getDefaultForType(key.getType());

            attributes.put(key, defaultValue);

            Debug.severe("Value provided for key " + key + " cannot be cast to " + key.getType().getSimpleName()
                    + ": " + value + ". Using default value: " + defaultValue);
        }
    }

    // Convert the string to the target type
    @SuppressWarnings("unchecked")
    private <T> T convertToTargetType(AttributeKey key, String value)
    {
        Class<?> type = key.getType();
        try
        {
            if (type == Integer.class)
            {
                return (T) Integer.valueOf(value);
            } else if (type == Double.class)
            {
                return (T) Double.valueOf(value);
            } else if (type == Float.class)
            {
                return (T) Float.valueOf(value);
            } else if (type == Boolean.class)
            {
                return (T) Boolean.valueOf(value);
            } else if (type == String.class)
            {
                return (T) value; // No conversion needed for String
            } else
            {
                throw new IllegalArgumentException("Unsupported type: " + type.getSimpleName());
            }
        } catch (Exception e)
        {
            throw new IllegalArgumentException("Conversion failed for key: " + key + " with value: " + value, e);
        }
    }

    // Get the default value for the target type
    @SuppressWarnings("unchecked")
    private <T> T getDefaultForType(Class<?> type)
    {
        if (type == Integer.class)
        {
            return (T) Integer.valueOf(-1);
        } else if (type == Double.class)
        {
            return (T) Double.valueOf(-1.0);
        } else if (type == Float.class)
        {
            return (T) Float.valueOf(-1.0f);
        } else if (type == Boolean.class)
        {
            return (T) Boolean.FALSE;
        } else if (type == String.class)
        {
            return (T) "Unknown";
        } else
        {
            throw new IllegalArgumentException("Unsupported type: " + type.getSimpleName());
        }
    }


    // Getter
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(AttributeKey key)
    {
        Object value = attributes.get(key);

        if (value == null)
        {
            return null;
        }

        if (!key.getType().isInstance(value))
        {
            throw new IllegalArgumentException("Stored value for key " + key + " is not of type " + key.getType().getSimpleName());
        }

        return (T) value;
    }

}

