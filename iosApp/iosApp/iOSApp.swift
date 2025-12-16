import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        initKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
