package greenmoonsoftware.gopherwave.intellij;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorEventMulticaster;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtilCore;
import greenmoonsoftware.es.event.SimpleEventBus;
import greenmoonsoftware.es.samples.docs.events.DocumentCreatedEvent;
import greenmoonsoftware.gopherwave.intellij.client.JavaSerializationClient;
import greenmoonsoftware.gopherwave.intellij.selection.GopherwaveSelectionListener;
import greenmoonsoftware.gopherwave.intellij.selection.TextSelectionChangedEventSubscriber;
import greenmoonsoftware.gopherwave.intellij.text.GopherwaveDocumentListener;
import greenmoonsoftware.gopherwave.intellij.text.TextChangedEventSubscriber;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class GopherwaveProjectComponent implements ProjectComponent {
    private final Logger log = Logger.getInstance(this.getClass());
    private Project project;
    private JavaSerializationClient service;
    private SimpleEventBus eventBus = new SimpleEventBus();

    private GopherwaveDocumentListener documentListener;

    public GopherwaveProjectComponent(Project project) {
        this.project = project;
    }

    public void initComponent() {
        ConfigFile config = new ConfigFile(project.getBasePath()+"/.gopherwave");
        if (config.enabled) {
            log.info("Found config file. Continuing to configure plugin");
            configure(config);
        }
        else {
            log.warn("Plugin is not enabled. Please ensure that the \"enabled\" property is set to \"true\" or review the idea.log for any error messages.");
        }
    }

    private void configure(ConfigFile config) {
        configureConnection(config);
        configureDocumentListener();
        configureEventBus();
    }

    private void configureEventBus() {
        eventBus.register(new DocumentEventSubscriber(project, documentListener))
                .register(new TextChangedEventSubscriber(project, documentListener))
                .register(new TextSelectionChangedEventSubscriber(project, documentListener));
    }

    private void configureDocumentListener() {
        EditorEventMulticaster em = EditorFactory.getInstance().getEventMulticaster();
        IntellijFileDocumentManager documentManager = new IntellijFileDocumentManager(project);
        documentListener = new GopherwaveDocumentListener(documentManager, project, service);
        em.addDocumentListener(documentListener);
        em.addSelectionListener(new GopherwaveSelectionListener(project, service, documentManager));
    }

    private void configureConnection(ConfigFile config) {
        service = ServiceManager.getService(JavaSerializationClient.class);
        service.setEventBus(eventBus);
        service.start(config.serverAddress, config.serverPort);
    }

    private void processFiles() {
        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        fileIndex.iterateContent(virtualFile -> {
            if (virtualFile.isDirectory()) {
                return true;
            }
            Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
            if (document == null) {
                return true;
            }
            String relativePath = VfsUtilCore.getRelativePath(virtualFile, project.getBaseDir(), '/');
            service.raiseEvents(Collections.singletonList(new DocumentCreatedEvent(relativePath, document.getText())));
            return true;
        });
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return "GopherwaveProjectComponent";
    }

    public void projectOpened() {
//        processFiles();
    }

    public void projectClosed() {
        // called when project is being closed
    }

    private class ConfigFile {
        boolean enabled = false;
        String serverAddress = "localhost";
        int serverPort = 16969;

        private ConfigFile(String filepath) {
            Properties props = getProperties(filepath);
            if (props != null) {
                setMembers(props);
            }
        }

        private void setMembers(Properties props) {
            enabled = Boolean.parseBoolean(props.getProperty("enabled", "true"));
            serverAddress = props.getProperty("serverAddress", "localhost");
            serverPort = Integer.parseInt(props.getProperty("serverPort", "16969"));
        }

        private Properties getProperties(String filepath) {
            try {
                return loadProperties();

            } catch (IOException e) {
                log.warn("Unable to read plugin configuration file: \"" + filepath + "\"", e);
                return null;
            }
        }

        private Properties loadProperties() throws IOException {
            Properties props = new Properties();
            FileInputStream fileStream = new FileInputStream(project.getBasePath() + "/.gopherwave");
            props.load(fileStream);
            return props;
        }
    }
}
