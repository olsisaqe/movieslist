import SwiftUI

struct ContentView: View {
    @ObservedObject var store: MoviesStore
    
    var body: some View {
        Text("Hello World").onAppear(perform: store.fetch)
    }
}
