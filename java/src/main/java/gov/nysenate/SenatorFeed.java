package gov.nysenate;

import java.util.List;
import java.util.Map;

/**
 * Internal model of the NYSenate.gov senators feed (/nys_feeds/senators), used only for
 * deserialization. The feed is mapped onto the flat {@link Senator} model, which is the public API.
 * Property names are mapped from snake_case by the client's ObjectMapper.
 */
record SenatorFeed(String status, List<String> messages, Data data) {

    record Data(Integer count, Map<String, Entry> senators) {}

    record Entry(OpenLeg openLeg, Profile profile, Contact contact, Map<String, String> socialMedia) {

        Senator toSenator() {
            var senator = new Senator();
            if (openLeg != null) {
                senator.setShortName(openLeg.shortname());
                senator.setOpenLegId(parseInteger(openLeg.memberId()));
                senator.setActive(openLeg.isActive());
                if (openLeg.district() != null) {
                    senator.setDistrictNumber(openLeg.district().number());
                    senator.setDistrictOrdinal(openLeg.district().ordinal());
                }
            }
            if (profile != null) {
                if (profile.name() != null) {
                    senator.setFullName(profile.name().full());
                    senator.setFirstName(profile.name().first());
                    senator.setLastName(profile.name().last());
                }
                senator.setSummary(profile.summary());
                senator.setParties(profile.party());
                senator.setRole(profile.role());
                if (profile.site() != null) {
                    senator.setUrl(profile.site().url());
                    senator.setDistrictUrl(profile.site().districtUrl());
                    senator.setPalette(profile.site().palette());
                    if (profile.site().images() != null) {
                        senator.setImageUrl(profile.site().images().headshot());
                        senator.setBannerImageUrl(profile.site().images().banner());
                    }
                }
            }
            if (contact != null) {
                senator.setEmail(contact.email());
                senator.setOffices(contact.offices());
            }
            senator.setSocialMedia(socialMedia == null ? Map.of() : socialMedia);
            return senator;
        }

        private static Integer parseInteger(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            try {
                return Integer.valueOf(value.trim());
            }
            catch (NumberFormatException ex) {
                return null;
            }
        }
    }

    record OpenLeg(String memberId, String shortname, District district, Boolean isActive) {}

    record District(Integer number, String ordinal) {}

    record Profile(Name name, String summary, List<String> party, String role, Site site) {}

    record Name(String full, String first, String last) {}

    record Site(String url, String districtUrl, Palette palette, Images images) {}

    record Images(String headshot, String banner) {}

    record Contact(String email, List<Office> offices) {}
}
