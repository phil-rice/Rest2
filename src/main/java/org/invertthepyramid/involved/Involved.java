package org.invertthepyramid.involved;

import org.invertthepyramid.involved.misc.DeleteINGPartyWithAncestors;
import org.invertthepyramid.involved.misc.SearchIndividualRequest;
import org.invertthepyramid.involved.domain.ContactMethod;
import org.invertthepyramid.involved.domain.Contract;
import org.invertthepyramid.involved.domain.PartyAddress;
import org.invertthepyramid.involved.domain.Person;
import org.invertthepyramid.involved.search.PartySearchResponse;
import org.invertthepyramid.involved.search.SearchOrganisationRequest;

import java.util.List;

public interface Involved {
    PartyAddress updateAddress(PartyAddress partyAddress, String lastUpdateUser, List<String> userRoles);

    PartyAddress getAddress(String partyAddressIdPK, List<String> userRoles);

    ContactMethod getContactMethod(String partyContactMethodIdPK, boolean keepObjectAlive, List<String> userRoles);

    ContactMethod updateContactMethod(ContactMethod contactMehod, String lastUpdateUser, List<String> userRoles);

    public Contract searchContract(String contractType, String contractId, List<String> userRoles);

    public Person getPartyDetails(String partyId, List<String> userRoles);

    public PartySearchResponse searchParty(SearchIndividualRequest individualSearch, SearchOrganisationRequest organisationSearch, List<String> userRoles);

    public DeleteINGPartyWithAncestors deleteParty(String adminSysKeyType, String adminSysKey, List<String> userRoles);
}
