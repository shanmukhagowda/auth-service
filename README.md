CSV
Controller
  @GetMapping("/export-to-csv")
	    public void exportIntoCSV(HttpServletResponse response) throws IOException {
	      response.setContentType("text/csv");
	      response.addHeader("Content-Disposition", "attachment; filename=\"student.csv\"");
	      fileStorageService.writeStudentsToCsv( response.getWriter());
	    }

     Service
@Override
	public void writeStudentsToCsv(Writer writer) throws IOException {
		try {
			writer.append("Id,studentName,email,mobileNo\n"); 
			CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

			List<Student> students = this.repository.findAll();
			for (Student student : students) {
			printer.printRecord(student.getId(), student.getStudentName(), student.getEmail(),
			student.getMobileNo());
			
			}
			
			writer.flush();
			} catch (IOException e) {
			e.printStackTrace();
			}
		
	}
--------------------------------------------------------------------------------------------------------
pdf

controller
 @GetMapping("/export-pdf")
	    public ResponseEntity<byte[]> exportToPdf() {
	    	ByteArrayOutputStream out = null;
			try {
				out = fileStorageService.exportDataToPdf(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        // Set headers for the PDF file download
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exported_data.pdf");
	        headers.setContentType(MediaType.APPLICATION_PDF);

	        return ResponseEntity
	                .ok()
	                .headers(headers)
	                .body(out.toByteArray());
	    }

     service
     @Override
	public ByteArrayOutputStream exportDataToPdf(String filePath) throws FileNotFoundException {
		List<Student> students = this.repository.findAll();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
        	// Initialize PDF writer
            PdfWriter writer = new PdfWriter(out);
            // Initialize PDF document
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            // Initialize document
            Document document = new Document(pdfDoc);

            // Add a title
            document.add(new Paragraph("Exported Data").setBold().setFontSize(20));

            // Create a table with the same number of columns as your entity fields
            Table table = new Table(new float[]{2, 2, 2, 2}); // Adjust column widths as necessary
            table.addHeaderCell("ID");
            table.addHeaderCell("Student Name"); // Replace with your actual field names
            table.addHeaderCell("Email");
            table.addHeaderCell("Mobile No");

            // Add data rows
            for (Student entity : students) {
                table.addCell(entity.getId().toString());
                table.addCell(entity.getStudentName()); // Replace with your actual getter methods
                table.addCell(entity.getEmail());
                table.addCell(entity.getMobileNo());
            }

            // Add table to the document
            document.add(table);

            // Close document
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
        return out;
	}
---------------------------------------------------------------------------------------------------------------------------------------
 file upload
Controller
 @PostMapping("/uploadFile")
	    public ApiResponse<?> uploadFile(@RequestParam("file") MultipartFile file) {
	        String fileName = null;
			try {
				fileName = fileStorageService.saveFile(file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/downloadFile/")
	            .path(fileName)
	            .toUriString();

	        return new ApiResponse<>(true, "SUCCESS", HttpStatus.OK.toString(), fileDownloadUri);
	    }

     Service
     public FileUploadServiceImpl(FileStorageProperties fileStorageProperties) throws Exception {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
		}
	}
