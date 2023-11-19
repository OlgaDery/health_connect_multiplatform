import SwiftUI
import shared
import CoreData
import UIKit
import HealthKit
import CoreLocation

struct ComposeView: UIViewControllerRepresentable {
    
    let locationManager = CLLocationManager()
    let healthStore = HKHealthStore()
    let dependManager: DependencyManager
    
    init() {
        dependManager = DependencyManager(locationNativeClient: locationManager, healthStore: healthStore)
        locationManager.requestWhenInUseAuthorization()
        checkHealthDataStatus()

    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        
        return PlatformKt.MainViewController(dependencyManager: dependManager)
    }
    
    private func checkHealthDataStatus() {
        if (HKHealthStore.isHealthDataAvailable()) {
            // Add code to use HealthKit here.
            let infoToRead = Set([
                HKSampleType.quantityType(forIdentifier: HKQuantityTypeIdentifier.basalBodyTemperature)!,
                HKSampleType.quantityType(forIdentifier: HKQuantityTypeIdentifier.heartRate)!,
                HKSampleType.quantityType(forIdentifier: HKQuantityTypeIdentifier.bloodPressureSystolic)!,
                HKSampleType.quantityType(forIdentifier: HKQuantityTypeIdentifier.bloodPressureDiastolic)!,
                HKObjectType.quantityType(forIdentifier: HKQuantityTypeIdentifier.stepCount)!
            ])

            healthStore.requestAuthorization(toShare: nil, read: infoToRead, completion: { (success, error) in

                if let error = error {
                    print("HealthKit Authorization Error: \(error.localizedDescription)")
                } else {
                    if success {
                        //we need to launch a job to collect the data
                        dependManager.healthRepo.launchDataCollectionJob()

                    } else {
                        print("HealthKit authorization did not complete successfully.")
                    }
                }
            })
        } else {
            print("health data not available")
        }
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}


struct ContentView: View {

    var body: some View {
        ComposeView()
    }
    
}
    

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
