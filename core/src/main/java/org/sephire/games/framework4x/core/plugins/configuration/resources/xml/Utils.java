package org.sephire.games.framework4x.core.plugins.configuration.resources.xml;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.sephire.games.framework4x.core.plugins.configuration.ConfigFileNotFoundException;
import org.sephire.games.framework4x.core.plugins.configuration.resources.xml.civilizations.Civilizations;
import org.sephire.games.framework4x.core.plugins.configuration.resources.xml.common.I18NString;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import static org.sephire.games.framework4x.core.plugins.configuration.resources.xml.Utils.Schema.getFullSchema;


/**
 * Utility methods and classes related to XML serialization
 */
public class Utils {

	private static final String PACKAGE_SEPARATOR = ":";

	/**
	 * This is the list of packages that contain jaxb xml files to be part of the context path
	 * for the marshaller/unmarshaller
	 */
	private static String XML_DTOS_PACKAGE = ""
	  + Civilizations.class.getPackageName()
	  + PACKAGE_SEPARATOR
	  + I18NString.class.getPackageName();

	/**
	 * Utility methods related to obtaining marshallers and unmarshallers for the main xml resource entities
	 * of the 4X framework.
	 */
	public static class Marshalling {

		private static Marshaller marshaller;
		private static Unmarshaller unmarshaller;
		private static JAXBContext jaxbContext;

		/**
		 * <p>Gets a marshaller capable of understanding all core XML resources.</p>
		 * <p>It is conveniente to get the marshaller through this method, since it will be cached and the cost
		 * thus incurred only once. Also it comes preconfigured with default properties.</p>
		 * @return
		 */
		public static Try<Marshaller> getMarshaller() {
			return Try.of(()->{
				if(marshaller == null) {
					var context = getContext().getOrElseThrow(t->t);
					marshaller = context.createMarshaller();
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
					marshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");
					marshaller.setSchema(getFullSchema().getOrElseThrow(t->t));
				}

				return marshaller;
			});
		}

		/**
		 * <p>Gets an unmarshaller capable of understanding all core XML resources</p>
		 * <p>It is convenient to get the unmarsheller through this methods, since it will be cached and also
		 * preconfigured</p>
		 * @return
		 */
		public static Try<Unmarshaller> getUnmarshaller() {
			return Try.of(()->{
				if(unmarshaller == null) {
					var context = getContext().getOrElseThrow(t->t);

					unmarshaller = context.createUnmarshaller();
					unmarshaller.setSchema(getFullSchema().getOrElseThrow(t->t));
				}

				return unmarshaller;
			});
		}

		private static Try<JAXBContext> getContext() {
			return Try.of(()->{
				if(jaxbContext == null) {
					jaxbContext = JAXBContextFactory.createContext(XML_DTOS_PACKAGE,Utils.class.getClassLoader());
				}

				return jaxbContext;
			});
		}

	}

	/**
	 * Utility methods related to schema generation and validation
	 */
	public static class Schema {
		private static javax.xml.validation.Schema fullSchema;

		public final static String CIVILIZATIONS_SCHEMA_FILE = "resources/schemas/civilizations.xsd";
		public final static String COMMON_TYPES_SCHEMA_FILE = "resources/schemas/common-types.xsd";

		/**
		 * Get the full schema for all resource types in 4xframework
		 * @return
		 */
		public static Try<javax.xml.validation.Schema> getFullSchema() {
			var result = fullSchema != null ?
			  Try.success(fullSchema) :
			  Try.of(()->{
					var civilizationsSchema = Schema.class.getClassLoader().getResourceAsStream(CIVILIZATIONS_SCHEMA_FILE);
					if (civilizationsSchema == null) {
						throw new ConfigFileNotFoundException(CIVILIZATIONS_SCHEMA_FILE);
					}
					var commonTypesSchema = Schema.class.getClassLoader().getResourceAsStream(COMMON_TYPES_SCHEMA_FILE);
					if(commonTypesSchema == null) {
						throw new ConfigFileNotFoundException(COMMON_TYPES_SCHEMA_FILE);
					}

					SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

					return schemaFactory.newSchema(List.of(
					  new StreamSource(commonTypesSchema),
					  new StreamSource(civilizationsSchema)).toJavaArray(StreamSource.class));
				}).peek(s->fullSchema = s);

			return result;
		}
	}
}
