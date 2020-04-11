package tdd.auction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tdd.auction.server.AuctionServer;
import tdd.auction.server.InMemoryAuctionServer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuctionSniperBiddingTest {

    AuctionServer auctionServer = new InMemoryAuctionServer();
    AuctionEventConsumer consumer = new AuctionEventConsumer(auctionServer);

    @BeforeEach
    public void bootstrapAuctionServer() {
        auctionServer.start();
    }

    @Test
    public void placesBidButLoseAuction() {

        auctionServer.startSelling("itemName-10", 100);

        auctionServer.join("itemName-10", "ABCL Corp", consumer);

        consumer.placeBid(10);

        auctionServer.auctionClosed();

        assertEquals(110, consumer.lastPrice());
        assertEquals(AuctionState.Lost, consumer.auctionState());

    }

    @AfterEach
    public void disconnect() {
        auctionServer.disconnect();
    }

}
