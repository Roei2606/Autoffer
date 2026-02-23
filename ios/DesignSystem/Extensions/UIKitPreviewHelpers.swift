import UIKit
import SwiftUI

struct UIViewPreview<V: UIView>: UIViewRepresentable {
    let builder: () -> V
    init(_ builder: @escaping () -> V) { self.builder = builder }
    func makeUIView(context: Context) -> V { builder() }
    func updateUIView(_ view: V, context: Context) {}
}

struct UIViewControllerPreview<C: UIViewController>: UIViewControllerRepresentable {
    let builder: () -> C
    init(_ builder: @escaping () -> C) { self.builder = builder }
    func makeUIViewController(context: Context) -> C { builder() }
    func updateUIViewController(_ vc: C, context: Context) {}
}
