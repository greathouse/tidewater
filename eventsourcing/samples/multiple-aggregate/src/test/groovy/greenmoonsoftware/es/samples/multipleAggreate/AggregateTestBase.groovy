package greenmoonsoftware.es.samples.multipleAggreate
import greenmoonsoftware.es.command.Processor
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.es.event.SimpleEventBus
import greenmoonsoftware.es.samples.user.User
import greenmoonsoftware.es.samples.user.commands.CreateUserCommand
import greenmoonsoftware.es.samples.user.commands.VerifyUserCommand
import greenmoonsoftware.es.store.StoreRetrieval
import org.testng.annotations.Test

abstract class AggregateTestBase {
    Processor commandProcessor
    protected SimpleEventBus eventBus

    abstract StoreRetrieval<User> getQuery();
    abstract EventSubscriber<Event> getStore();

    @Test
    void givenCreateUserEvents_shouldCreateMultipleUsers() {
        createAndRegisterEventSubscriber()

        def (UUID robert, String robertFullName) = createUser('Robert Greathouse')
        def (UUID abram, String abramFullname) = createUser('Abraham Greathouse')

        assertCreated(robert, robertFullName)
        assertCreated(abram, abramFullname)

        verify(robert)
        assertVerified(robert)
        assertNotVerified(abram)
    }

    private void assertCreated(UUID aggregateId, String expectedFullName) {
        def actual = query.retrieve(aggregateId.toString())
        assert actual.fullname == expectedFullName
        assert actual.state == User.State.REGISTERED
    }

    private void assertVerified(UUID aggregateId) {
        def actual = query.retrieve(aggregateId.toString())
        assert actual.state == User.State.VERIFIED
    }

    private void assertNotVerified(UUID aggregateId) {
        def actual = query.retrieve(aggregateId.toString())
        assert actual.state == User.State.REGISTERED
    }

    private List createUser(String fullname) {
        def robert = UUID.randomUUID()
        def expectedFullName = fullname
        commandProcessor.process(new CreateUserCommand(robert.toString(), expectedFullName))
        [robert, expectedFullName]
    }

    private void verify(UUID aggregateId) {
        commandProcessor.process(new VerifyUserCommand(aggregateId.toString()))
    }

    private void createAndRegisterEventSubscriber() {
        eventBus = new SimpleEventBus()
        eventBus.register(store)
        commandProcessor = new Processor(query, eventBus)
    }
}
