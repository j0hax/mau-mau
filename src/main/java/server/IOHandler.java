package server;


class IOHandler {
    private String packet;

    // True if receiver should wait
    // False if sender should wait
    private boolean currentlyNewDataAvailable = false;

    synchronized void send(String packet) {
        while (currentlyNewDataAvailable) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        currentlyNewDataAvailable = true;

        this.packet = packet;
        notifyAll();
    }

    synchronized String receive() {
        while (!currentlyNewDataAvailable) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        currentlyNewDataAvailable = false;

        notifyAll();
        return packet;
    }
}
