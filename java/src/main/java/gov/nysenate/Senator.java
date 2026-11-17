package gov.nysenate;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Senator {
    private String fullName;
    private String firstName;
    private String lastName;
    private String shortName;
    private String email;
    private String imageUrl;
    private String url;
    private String summary;
    private List<String> parties;
    private String role;
    private List<Office> offices;
    private Integer districtNumber;
    private String districtUrl;
    private Integer openLegId;
    private Boolean active;
    private String districtOrdinal;
    private String bannerImageUrl;
    private Palette palette;
    /** Social media URLs keyed by network name, e.g. "facebook", "twitter", "instagram", "youtube". */
    private Map<String, String> socialMedia;

    public Senator() {}
}
