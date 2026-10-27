package gov.nysenate;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("img")
    private String imageUrl;
    private String url;
    private String summary;
    @JsonProperty("party")
    private List<String> parties;
    private String role;
    private List<Office> offices;
    @JsonProperty("senate_district")
    private Integer districtNumber;
    @JsonProperty("senate_district_url")
    private String districtUrl;
    private Integer openLegId;

    public Senator() {}
}
