package lab.orders;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

@AnalyzeClasses(packages = "lab.orders", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {
    /** The domain may only use the JDK basics and itself: zero framework imports. */
    static final ArchRule DOMAIN_IS_FRAMEWORK_FREE = noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideOutsideOfPackages("..domain..", "java.lang..", "java.util..", "java.time..");

    static final ArchRule LAYERS = layeredArchitecture().consideringOnlyDependenciesInLayers()
            .layer("Adapter").definedBy("..adapter..")
            .layer("Application").definedBy("..application..")
            .layer("Domain").definedBy("..domain..")
            .whereLayer("Adapter").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapter")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Adapter");

    @ArchTest
    static final ArchRule domainIsFrameworkFree = DOMAIN_IS_FRAMEWORK_FREE;

    @ArchTest
    static final ArchRule layersPointInward = LAYERS;

    /** Proves the rules bite: a deliberately bad fixture (domain importing javax.swing) must fail. */
    @Test
    void rulesRejectViolatingFixture() {
        JavaClasses bad = new ClassFileImporter().importPackages("lab.fixture");
        assertThrows(AssertionError.class, () -> DOMAIN_IS_FRAMEWORK_FREE.check(bad));
    }
}
