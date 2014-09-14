package org.maxur.akkacluster;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.CurrentClusterState;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SimpleClusterListener extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(context().system(), this);

    private Cluster cluster = Cluster.get(context().system());

    @Override
    public void preStart() {
        cluster.subscribe(self(), MemberEvent.class, UnreachableMember.class);
    }

    @Override
    public void postStop() {
        cluster.unsubscribe(self());
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof CurrentClusterState) {
            CurrentClusterState state = (CurrentClusterState) message;
            logger.info("Current members: {}", state.members());

        } else if (message instanceof MemberUp) {
            MemberUp mUp = (MemberUp) message;
            logger.info("Member is Up: {}", mUp.member());

        } else if (message instanceof UnreachableMember) {
            UnreachableMember mUnreachable = (UnreachableMember) message;
            logger.info("Member detected as unreachable: {}", mUnreachable.member());

        } else if (message instanceof MemberRemoved) {
            MemberRemoved mRemoved = (MemberRemoved) message;
            logger.info("Member is Removed: {}", mRemoved.member());

        } else if (message instanceof MemberEvent) {
            // ignore

        } else {
            unhandled(message);
        }

    }
}
