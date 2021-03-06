/*
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.hska.bestellung.config.dev;

import de.hska.bestellung.service.KundeClient;

import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;

import static de.hska.bestellung.config.Settings.DEV_PROFILE;
import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Registrierte Services f&uuml;r den aufzurufenden Microservice "bestellung"
 * protokollieren.
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">
 *         J&uuml;rgen Zimmermann</a>
 */
public interface LogDiscoveryClient {
    @Bean
    @Qualifier("LogDiscoveryClientRunner")
    @Description("Ausgabe der registrierten Services")
    @Profile(DEV_PROFILE)
    default CommandLineRunner logDiscoveryClientRunner(
        @SuppressWarnings("SpringJavaAutowiringInspection")
            DiscoveryClient discoveryClient) {
        final val log = getLogger();
        final val services = discoveryClient.getServices();
        final val serviceInstanzen =
            discoveryClient.getInstances(KundeClient.SERVICE_NAME);


        return args -> {
            log.warn("Registrierte Services: {}", services);

            if (serviceInstanzen.isEmpty()) {
                log.error("Es ist kein Service mit dem Namen {} registriert",
                        KundeClient.SERVICE_NAME);
                return;
            }

            log.warn("Instanzen des Service {}:",
                     KundeClient.SERVICE_NAME);
            serviceInstanzen.forEach(s -> log.warn("   URI = {}", s::getUri));
        };
    }
}
