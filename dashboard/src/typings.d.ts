declare module 'sockjs-client';

declare module 'jspdf-autotable' {
    import jsPDF from 'jspdf';
    export interface jsPDFDocument extends jsPDF {
      autoTable: (options: any) => jsPDF;
    }
    const autotable: (doc: jsPDF, options: any) => jsPDF;
    export default autotable;
  }
  