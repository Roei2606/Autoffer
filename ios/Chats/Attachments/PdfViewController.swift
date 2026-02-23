import UIKit
import PDFKit

final class PdfViewController: UIViewController {
    private let pdfView = PDFView()
    var pdfUrl: URL?

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        title = "PDF Viewer"

        setupPdfView()

        if let url = pdfUrl {
            openPdf(from: url)
        }
    }

    private func setupPdfView() {
        view.addSubview(pdfView)
        pdfView.translatesAutoresizingMaskIntoConstraints = false
        pdfView.autoScales = true
        pdfView.displayMode = .singlePageContinuous
        pdfView.displayDirection = .vertical
        pdfView.usePageViewController(true)

        NSLayoutConstraint.activate([
            pdfView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            pdfView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            pdfView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            pdfView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }

    private func openPdf(from url: URL) {
        if let document = PDFDocument(url: url) {
            pdfView.document = document
        } else {
            let alert = UIAlertController(
                title: "Error",
                message: "Unable to open PDF",
                preferredStyle: .alert
            )
            alert.addAction(UIAlertAction(title: "OK", style: .default))
            present(alert, animated: true)
        }
    }
}
