package greenmoonsoftware.test

class RetryableAssert {
    static void run(Closure c) {
        def start = System.currentTimeMillis()
        while (true) {
            try {
                c()
                break
            }
            catch (Throwable all) {
                sleep 250
                def processTime = (System.currentTimeMillis() - start) / 1000
                if (processTime > 5) {
                    throw all
                }
            }
        }
    }
}
