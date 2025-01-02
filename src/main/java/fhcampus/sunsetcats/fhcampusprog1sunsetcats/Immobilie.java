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

        PRICE("price", Double.class),
        PRICE_DISPLAY("priceDisplay", String.class),
        RENT_MONTH("rentperMonth", Double.class),
        PRICE_SUGGESTION("priceSuggestion", Double.class),
        PROJECT_UNIT_PRICE_FROM("projectUnitPriceFrom", String.class),
        PROJECT_UNIT_RENT_FROM("projectUnitRentFrom", String.class),

        POSTCODE("postcode", Integer.class),
        DISTRICT("district", String.class),
        ADDRESS("address", String.class),
        LOCATION("location", String.class),
        STATE("state", String.class),
        COUNTRY("country", String.class),
        COORDINATES("coordinates", String.class),

        UNIT_NUMBER("unitNumber", String.class),

        ESTATE_SIZE_TOTAL("estateSizeTotal", Double.class),
        ESTATE_SIZE_LIVING_AREA("estateSizeLivingArea", Double.class),
        ESTATE_SIZE_USEABLE_AREA("estateSizeUseableArea", Double.class),

        FREE_AREA_TOTAL("freeAreaTotal", Double.class),
        FREE_AREA_TYPE("freeAreaType", String.class),
        FREE_AREA_TYPE_NAME("freeAreaTypeName", String.class),

        NUMBER_OF_ROOMS("numberOfRooms", Integer.class),
        ROOMS("rooms", Integer.class),
        FLOOR("floor", Integer.class),
        NUMBER_OF_CHILDREN("numberOfChildren", Integer.class),

        BODY_DYN("bodyDyn", String.class),

        PROPERTY_TYPE_ID("propertyTypeId", String.class),
        PROPERTY_TYPE_HOUSE("propertyTypeHouse", String.class),
        PROPERTY_TYPE_FLAT("propertyTypeFlat", String.class),

        VIRTUAL_VIEW_LINK("virtualViewLink", String.class),
        IMAGE_DESCRIPTION("imageDescription", String.class),
        ALL_IMAGE_URLS("allImageUrls", String.class),

        ORGNAME("organizationName", String.class),
        ORGID("orgId", String.class),
        ORG_UUID("orgUuid", String.class),

        LOCATION_ID("locationId", String.class),
        LOCATION_QUALITY("locationQuality", String.class),

        HEADING("heading", String.class),
        PUBLISHED("published", String.class),
        PUBLISHED_STRING("publishedString", String.class),

        SEO_URL("seoUrl", String.class),
        ADID("adId", String.class),
        ADVERTISER_REF("advertiserRef", String.class),
        UPSELLING_AD_SEARCHRESULT("upsellingAdSearchResult", String.class),

        ESTATE_PREFERENCE("estatePreference", String.class),
        CATEGORY_TREE_IDS("categoryTreeIds", String.class),
        IS_BUMPED("isBumped", String.class),
        MMO("mmo", String.class),
        AD_UUID("adUuid", String.class),
        IS_PRIVATE("isPrivate", String.class),
        UNIT_TITLE("unitTitle", String.class),
        AD_SEARCHRESULT_LOGO("adSearchResultLogo", String.class),
        PROJECT_ID("projectId", String.class);


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

            if(value == null) { return; }

            Debug.warning("Value provided for Key {RED}" + key + "{WHITE} cannot be cast to {BLUE}" + key.getType().getSimpleName()
                    + ": " + value + ".{WHITE} Using default value: " + defaultValue);
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

