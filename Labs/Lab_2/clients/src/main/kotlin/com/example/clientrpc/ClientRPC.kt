package com.example.clientrpc


import com.example.state.IOUState
import net.corda.client.rpc.CordaRPCClient
import net.corda.core.contracts.StateAndRef
import net.corda.core.utilities.NetworkHostAndPort
import net.corda.core.utilities.loggerFor
import org.slf4j.Logger

/**
 *  Demonstration of using the CordaRPCClient to connect to a Corda Node and steam some State data from the node.
 *  Connects to a Corda Node via RPC and performs RPC operations on the node.
 **/

fun main(args: Array<String>) {
    ExampleClientRPC().main(args)
}

private class ExampleClientRPC {
    companion object {
        val logger: Logger = loggerFor<ExampleClientRPC>()
        private fun logState(state: StateAndRef<IOUState>) = logger.info("{}", state.state.data)
    }

    fun main(args: Array<String>) {
        // Create an RPC connection to the node.
        require(args.size == 3) { "Usage: ExampleClientRPC <node address> <rpc username> <rpc password>" }
        val nodeAddress = NetworkHostAndPort.parse(args[0])
        val rpcUsername = args[1]
        val rpcPassword = args[2]

        val client = CordaRPCClient(nodeAddress)
        val proxy = client.start(rpcUsername, rpcPassword).proxy

        // Interact with the node.
        // For example 1, here we print the nodes on the network.
        val nodes = proxy.networkMapSnapshot()
        logger.info("{}", nodes)

        // For example 2, grab all existing and future IOU states in the vault.
        val (snapshot, updates) = proxy.vaultTrack(IOUState::class.java)
        // Log the 'placed' IOU states and listen for new ones.
        snapshot.states.forEach { logState(it) }
        updates.toBlocking().subscribe { update ->
            update.produced.forEach { logState(it) }
        }
    }
}