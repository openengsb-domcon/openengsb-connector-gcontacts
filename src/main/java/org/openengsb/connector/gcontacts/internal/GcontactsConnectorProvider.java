package org.openengsb.connector.gcontacts.internal;

import org.openengsb.core.api.descriptor.ServiceDescriptor;
import org.openengsb.core.api.descriptor.ServiceDescriptor.Builder;
import org.openengsb.core.common.AbstractConnectorProvider;

public class GcontactsConnectorProvider extends AbstractConnectorProvider {

    @Override
    public ServiceDescriptor getDescriptor() {
        Builder builder = ServiceDescriptor.builder(strings);
        builder.id(this.id);
        builder.name("service.name").description("service.description");

        builder.attribute(
            builder.newAttribute().id("google.user").name("google.user.name").description("google.user.description")
                .build());
        builder.attribute(builder.newAttribute().id("google.password").name("google.password.name")
            .description("google.password.description").asPassword().build());

        return builder.build();
    }

}
