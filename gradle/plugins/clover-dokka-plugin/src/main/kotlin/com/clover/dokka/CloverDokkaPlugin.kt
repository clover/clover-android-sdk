package com.clover.dokka

import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.transformers.pages.tags.CustomTagContentProvider
import org.jetbrains.dokka.base.translators.documentables.PageContentBuilder
import org.jetbrains.dokka.model.CheckedExceptions
import org.jetbrains.dokka.model.DAnnotation
import org.jetbrains.dokka.model.DClass
import org.jetbrains.dokka.model.DClasslike
import org.jetbrains.dokka.model.DEnum
import org.jetbrains.dokka.model.DEnumEntry
import org.jetbrains.dokka.model.DFunction
import org.jetbrains.dokka.model.DInterface
import org.jetbrains.dokka.model.DModule
import org.jetbrains.dokka.model.DObject
import org.jetbrains.dokka.model.DPackage
import org.jetbrains.dokka.model.Documentable
import org.jetbrains.dokka.model.WithCompanion
import org.jetbrains.dokka.model.doc.CustomDocTag
import org.jetbrains.dokka.model.doc.CustomTagWrapper
import org.jetbrains.dokka.model.doc.Throws
import org.jetbrains.dokka.model.properties.WithExtraProperties
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.plugability.DokkaPluginApiPreview
import org.jetbrains.dokka.plugability.PluginApiPreviewAcknowledgement
import org.jetbrains.dokka.transformers.documentation.PreMergeDocumentableTransformer

class CloverDokkaPlugin : DokkaPlugin() {
    @DokkaPluginApiPreview
    override fun pluginApiPreviewAcknowledgement(): PluginApiPreviewAcknowledgement = PluginApiPreviewAcknowledgement

    val cloverPermTagContentProvider by extending {
        plugin<DokkaBase>().customTagContentProvider with CloverPermTagContentProvider()
    }
    val checkedExceptionThrowsTransformer by extending {
        plugin<DokkaBase>().preMergeDocumentableTransformer with CheckedExceptionThrowsTransformer()
    }
}

// Adds a doc section for required clover perms whenever an @clover.perm block tag is encountered
class CloverPermTagContentProvider : CustomTagContentProvider {
    override fun isApplicable(customTag: CustomTagWrapper): Boolean = customTag.name == ANNOTATION

    override fun PageContentBuilder.DocumentableContentBuilder.contentForDescription(
        sourceSet: DokkaConfiguration.DokkaSourceSet,
        customTag: CustomTagWrapper
    ) {
        header(3, "Required Clover permission:")
        comment(customTag.root, sourceSets = setOf(sourceSet))
    }

    companion object {
        const val ANNOTATION = "clover.perm"
    }
}

// Goes through all function documentables (DFunction) and ensures that any checked exceptions that
//  aren't already documented via @throws/@exception block tags, are added to the documentation
class CheckedExceptionThrowsTransformer : PreMergeDocumentableTransformer {
    override fun invoke(modules: List<DModule>): List<DModule> = modules.map { module ->
        val (documentable, wasChanged) = processModule(module)
        documentable.takeIf { wasChanged } ?: module
    }
    private fun processModule(module: DModule): DocumentableWithChanges<DModule> {
        val afterProcessing = module.packages.map { processPackage(it) }
        val processedModule = module.takeIf { afterProcessing.none { it.changed } }
            ?: module.copy(packages = afterProcessing.mapNotNull { it.documentable })
        return DocumentableWithChanges(processedModule, afterProcessing.any { it.changed })
    }

    private fun processPackage(dPackage: DPackage): DocumentableWithChanges<DPackage> {
        val classlikes = dPackage.classlikes.map { processClassLike(it) }
        val typeAliases = dPackage.typealiases.map { DocumentableWithChanges(it, false) }
        val functions = dPackage.functions.map { processFunction(it) }
        val properies = dPackage.properties.map { DocumentableWithChanges(it, false) }

        val wasChanged = (classlikes + typeAliases + functions + properies).any { it.changed }
        return (dPackage.takeIf { !wasChanged } ?: dPackage.copy(
            classlikes = classlikes.mapNotNull { it.documentable },
            typealiases = typeAliases.mapNotNull { it.documentable },
            functions = functions.mapNotNull { it.documentable },
            properties = properies.mapNotNull { it.documentable }
        )).let { processedPackage ->
            DocumentableWithChanges(
                processedPackage,
                wasChanged
            )
        }
    }

    private fun processClassLike(classlike: DClasslike): DocumentableWithChanges<DClasslike> {
        val functions = classlike.functions.map { processFunction(it) }
        val classlikes = classlike.classlikes.map { processClassLike(it) }
        val properties = classlike.properties.map { DocumentableWithChanges(it, false) }
        val companion = (classlike as? WithCompanion)?.companion?.let { processClassLike(it) }

        val wasClasslikeChanged = (functions + classlikes + properties).any { it.changed } || companion?.changed == true
        return when (classlike) {
            is DClass -> {
                val constructors = classlike.constructors.map { processFunction(it) }
                val wasClassChange =
                    wasClasslikeChanged || constructors.any { it.changed }
                (classlike.takeIf { !wasClassChange } ?: classlike.copy(
                    functions = functions.mapNotNull { it.documentable },
                    classlikes = classlikes.mapNotNull { it.documentable },
                    properties = properties.mapNotNull { it.documentable },
                    constructors = constructors.mapNotNull { it.documentable },
                    companion = companion?.documentable as? DObject
                )).let { DocumentableWithChanges(it, wasClassChange) }
            }
            is DInterface -> (classlike.takeIf { !wasClasslikeChanged } ?: classlike.copy(
                functions = functions.mapNotNull { it.documentable },
                classlikes = classlikes.mapNotNull { it.documentable },
                properties = properties.mapNotNull { it.documentable },
                companion = companion?.documentable as? DObject
            )).let { DocumentableWithChanges(it, wasClasslikeChanged) }
            is DObject -> (classlike.takeIf { !wasClasslikeChanged } ?: classlike.copy(
                functions = functions.mapNotNull { it.documentable },
                classlikes = classlikes.mapNotNull { it.documentable },
                properties = properties.mapNotNull { it.documentable },
            )).let { DocumentableWithChanges(it, wasClasslikeChanged) }
            is DAnnotation -> {
                val constructors = classlike.constructors.map { processFunction(it) }
                val wasClassChange =
                    wasClasslikeChanged || constructors.any { it.changed }
                (classlike.takeIf { !wasClassChange } ?: classlike.copy(
                    functions = functions.mapNotNull { it.documentable },
                    classlikes = classlikes.mapNotNull { it.documentable },
                    properties = properties.mapNotNull { it.documentable },
                    constructors = constructors.mapNotNull { it.documentable },
                    companion = companion?.documentable as? DObject
                )).let { DocumentableWithChanges(it, wasClassChange) }
            }
            is DEnum -> {
                val constructors = classlike.constructors.map { processFunction(it) }
                val entries = classlike.entries.map { processEnumEntry(it) }
                val wasClassChange =
                    wasClasslikeChanged || (constructors + entries).any { it.changed }
                (classlike.takeIf { !wasClassChange } ?: classlike.copy(
                    functions = functions.mapNotNull { it.documentable },
                    classlikes = classlikes.mapNotNull { it.documentable },
                    properties = properties.mapNotNull { it.documentable },
                    constructors = constructors.mapNotNull { it.documentable },
                    companion = companion?.documentable as? DObject,
                    entries = entries.mapNotNull { it.documentable }
                )).let { DocumentableWithChanges(it, wasClassChange) }
            }
        }
    }

    private fun processEnumEntry(dEnumEntry: DEnumEntry): DocumentableWithChanges<DEnumEntry> {
        val functions = dEnumEntry.functions.map { processFunction(it) }
        val properties = dEnumEntry.properties.map { DocumentableWithChanges(it, false) }
        val classlikes = dEnumEntry.classlikes.map { processClassLike(it) }

        val wasChanged = (functions + properties + classlikes).any { it.changed }
        return (dEnumEntry.takeIf { !wasChanged } ?: dEnumEntry.copy(
            functions = functions.mapNotNull { it.documentable },
            classlikes = classlikes.mapNotNull { it.documentable },
            properties = properties.mapNotNull { it.documentable },
        )).let { DocumentableWithChanges(it, wasChanged) }
    }

    private fun processFunction(d: DFunction): DocumentableWithChanges<DFunction> {
        val checkedExceptions = (d as? WithExtraProperties<*>)
            ?.extra
            ?.allOfType<CheckedExceptions>()
            ?.flatMap { it.exceptions.values.flatten() }
            ?.takeIf { it.isNotEmpty() }
            ?: return DocumentableWithChanges(d, false)

        val doc = d.documentation.entries.single()
        val existingThrows = doc.value.children.filterIsInstance<Throws>();
        val throws = checkedExceptions
            // we don't want to generate duplicate Throws entries per exception type, so filter out
            // ones that already had a Throws generated by a comment @throws/@exception
            .filterNot { exe -> existingThrows.any { it.exceptionAddress == exe } }
            .map {
                Throws(
                    root = CustomDocTag(name = "MARKDOWN_FILE"),
                    name = "${it.packageName}.${it.classNames}",
                    exceptionAddress = it
                )
            }

        return DocumentableWithChanges(d.copy(
            documentation = mapOf(
                doc.key to doc.value.copy(
                    children = doc.value.children + throws
                )
            )
        ), true)
    }
    private data class DocumentableWithChanges<T : Documentable>(val documentable: T?, val changed: Boolean = false)
}