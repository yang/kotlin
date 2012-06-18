package test.kotlin

import org.jetbrains.jet.cli.jvm.compiler.JetCoreEnvironment
import org.junit.Before
import org.jetbrains.jet.internal.com.intellij.openapi.Disposable
import org.jetbrains.jet.lang.resolve.java.CompilerDependencies
import org.jetbrains.jet.lang.resolve.java.CompilerSpecialMode
import org.junit.After
import org.jetbrains.jet.internal.com.intellij.openapi.util.Disposer
import org.junit.Test
import org.jetbrains.jet.plugin.JetLanguage
import org.jetbrains.jet.internal.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.jet.internal.com.intellij.openapi.vfs.CharsetToolkit
import org.jetbrains.jet.internal.com.intellij.psi.PsiFile
import org.jetbrains.jet.lang.psi.JetFile
import org.jetbrains.jet.internal.com.intellij.psi.PsiFileFactory
import org.jetbrains.jet.internal.com.intellij.psi.impl.PsiFileFactoryImpl
import org.jetbrains.kotlin.doc.highlighter2.splitPsi
import org.junit.Assert
import java.util.List

class PsiUtilsTest {

    val rootDisposable = object : Disposable {
        public override fun dispose() {
        }
    }

    private var environment: JetCoreEnvironment? = null

    [Before]
    fun before() {
        System.setProperty("java.awt.headless", "true")

        val compilerDependencies = CompilerDependencies.compilerDependenciesForProduction(CompilerSpecialMode.JDK_HEADERS)
        environment = JetCoreEnvironment(rootDisposable, compilerDependencies)
    }

    [After]
    fun after() {
        Disposer.dispose(rootDisposable)
    }

    private fun createFile(content: String): JetFile {
        val virtualFile = LightVirtualFile("file.kt", JetLanguage.INSTANCE, content);
        virtualFile.setCharset(CharsetToolkit.UTF8_CHARSET);
        return (PsiFileFactory.getInstance(environment!!.getProject()) as PsiFileFactoryImpl)
                .trySetupPsiForFile(virtualFile, JetLanguage.INSTANCE, true, false) as JetFile
    }

    [Test]
    fun splitPsi() {
        val file = createFile("class Foo")
        val items: List<String> = splitPsi(file).map { t -> t._1 }
        Assert.assertEquals(arrayList("class", " ", "Foo"), items)
    }

}