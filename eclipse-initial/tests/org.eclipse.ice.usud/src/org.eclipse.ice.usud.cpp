//============================================================================
// Name        : usud.cpp
// Author      : Jay Jay Billings
// Version     : 2.0
//
// Copyright (c) 2012, 2014 UT-Battelle, LLC.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
//   Initial API and implementation and/or initial documentation - Jay Jay Billings,
//   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
//   Claire Saunders, Matthew Wang, Anna Wojtowicz
//
// Description : The Ultimate Simulator of Ultimate Destiny! This is a test
//               code for use as a "standard candle" job launcher that does a
//               minimal amount of work, has minimal dependencies, links with
//               and tests the C++ parts of ICE and does various other
//               useful things that no other code developed by a customer can.
//               It is not meant to be efficient, pretty or useful for any
//               purpose other than testing ICE.
//
//               The code can run simple tests in parallel with MPI, OpenMP
//               and Intel TBB (OpenMP and TBB pending).
//
//               To execute with MPI:
//
//               mpirun -np <nprocs> ./org.eclipse.ice.usud <input_file>
//
//               To execute with OpenMP:
//
//               OMP_NUM_THREADS=<nthreads> ./org.eclipse.ice.usud <input_file>
//============================================================================

#include <iostream>
#include <stdlib.h>
#include <ctime>
#include <mpi.h>
#include <string>
using namespace std;

// This operation prints diagnostic information the screen.
void printDiagnosticInfo(int argc, char **argv) {

	// Print boot message
	cout << "Booting ICE Test Code: USUD" << endl;
	cout << "This is the Ultimate Simulator of Ultimate Destiny!" << endl;

	// Get the current date
	time_t now = time(0);

	// Convert the date to a string
	char* dt = ctime(&now);

	cout << "Local time: " << dt << endl;

	// Convert the time to UTC
	tm *gmtm = gmtime(&now);
	dt = asctime(gmtm);
	cout << "UTC: " << dt << endl;

	// Print the command-line arguments
	cout << "Command line arguments:" << endl;
	for (int i = 0; i < argc; i++) {
		cout << "\t" << argv[i] << endl;
	}

	return;
}

/* Full disclosure: I no longer know what this code does *exactly.* I know
 * that it does a matrix multiply, but the precise details escape me. I wrote
 * this code when I was a graduate student at UT-Knoxville to learn how to
 * use MPI. I'll find some time to document it better when I get a chance
 * in the future.
 *
 * I am reusing this code here because the USUD is just a test code and
 * does not need to do any real work.
 *
 * Jay Jay Billings
 * Knoxville, TN
 * 20121221 - The End of the World
 */

void multiplyMatrices(int argc, char **argv) {

	MPI_Status status; //Defines MPI status variable to track packet information.
	int rows = 50;
	int cols = rows;
	int MASTER = 0;
	int FROM_MASTER = 1;
	int FROM_WORKER = 2;
	double matrix[rows][cols], matrix_2[rows][cols], answer_matrix[rows][cols];
	int procs, myproc, total_procs, source, destiny, tag, average_rows,
			extra_rows, start, sent_rows, l, m, n, pieces;

	// Initialize MPI
	MPI_Init(&argc, &argv);
	// Determines local process ID
	MPI_Comm_rank(MPI_COMM_WORLD, &myproc);
	// Determines the total number of processors and their process IDs
	MPI_Comm_size(MPI_COMM_WORLD, &procs);
	total_procs = procs - 1;

	// Master block - setup the matrices and distribute the rows to the work
	// tasks to be multiplied. The entire second matrix is transmitted.
	//
	// ... Give me a break... I was just learning how to do parallel
	// programming!
	if (myproc == MASTER) {

		// Print diagnostics
		printDiagnosticInfo(argc,argv);

		// Print a start message to flag the output
		cout << "----- Starting Matrix Multiply -----" << endl;
		cout << "\tNumber of MPI Processes = " << procs << endl;
		cout << "\tNumber of MPI Processes that are slaves = " << total_procs
				<< endl;

		// Zero out the matrices
		for (l = 0; l < rows; ++l) {
			for (m = 0; m < cols; ++m) {
				matrix[l][m] = 0.0;
				matrix_2[l][m] = 0.0;
			}
		}
		// Set the matrix elements. The first matrix is a diagonal identity
		// matrix and the second matrix is a diagonal negative identity matrix
		// with all diagonal elements equal to -1.
		for (l = 0; l < rows; l++) {
			matrix[l][l] = 1;
			matrix_2[l][l] = -1;
		}

		// Print the first matrix
		cout << "First matrix:" << endl;
		for (l = 0; l < rows; ++l) {
			for (m = 0; m < cols; ++m) {
				cout << matrix[l][m] << " ";
			}
			cout << endl;
		}

		// Print the second matrix
		cout << "Second matrix:" << endl;
		for (l = 0; l < rows; ++l) {
			for (m = 0; m < cols; ++m) {
				cout << matrix_2[l][m] << " ";
			}
			cout << endl;
		}

		average_rows = rows / total_procs;
		extra_rows = rows % total_procs;
		start = 0;
		tag = FROM_MASTER;

		for (destiny = 1; destiny <= total_procs; ++destiny) {
			sent_rows = (destiny <= extra_rows) ? average_rows + 1
					: average_rows;
			cout << "Sending " << sent_rows << " rows to task " << destiny
					<< endl;
			MPI_Send(&start, 1, MPI_INT, destiny, tag, MPI_COMM_WORLD);
			MPI_Send(&sent_rows, 1, MPI_INT, destiny, tag, MPI_COMM_WORLD);
			pieces = sent_rows * cols;
			MPI_Send(&matrix[start][0], pieces, MPI_DOUBLE, destiny, tag,
					MPI_COMM_WORLD);
			pieces = cols * cols;
			MPI_Send(&matrix_2, pieces, MPI_DOUBLE, destiny, tag,
					MPI_COMM_WORLD);
			start += sent_rows;
		}

		tag = FROM_WORKER;
		for (l = 1; l <= total_procs; ++l) {
			source = l;
			MPI_Recv(&start, 1, MPI_INT, source, tag, MPI_COMM_WORLD, &status);
			MPI_Recv(&sent_rows, 1, MPI_INT, source, tag, MPI_COMM_WORLD,
					&status);
			pieces = sent_rows * cols;
			MPI_Recv(&answer_matrix[start][0], pieces, MPI_DOUBLE, source, tag,
					MPI_COMM_WORLD, &status);
		}

		cout << "The answer is: " << endl;
		for (l = 0; l < rows; ++l) {
			for (m = 0; m < cols; ++m) {
				cout << answer_matrix[l][m] << " ";
			}
			cout << endl;
		}
		cout << endl;

		// Print exit message
		cout << "Stopping ICE Test Code: USUD" << endl;


	} else if (myproc > MASTER) {
		// Worker/slave block. Rows of matrix 1 are received from the master
		// and multiplied with columns of matrix 2, which is transmitted
		// completely from the master.
		tag = FROM_MASTER;
		source = MASTER;
		cout << "Solving on USUD Worker Task Id: " << myproc << endl;
		MPI_Recv(&start, 1, MPI_INT, source, tag, MPI_COMM_WORLD, &status);
		MPI_Recv(&sent_rows, 1, MPI_INT, source, tag, MPI_COMM_WORLD, &status);
		pieces = sent_rows * cols;
		MPI_Recv(&matrix, pieces, MPI_DOUBLE, source, tag, MPI_COMM_WORLD,
				&status);
		pieces = rows * cols;
		MPI_Recv(&matrix_2, pieces, MPI_DOUBLE, source, tag, MPI_COMM_WORLD,
				&status);

		// Print diagnostic information about the rows sent back to the master
		//for (l = 0; l < sent_rows; ++l) {
		//	for (m = 0; m < cols; ++m) {
		//		printf("%6.2f ", matrix[l][m]);
		//	}
		//	printf("\n");
		//}

		for (l = 0; l < sent_rows; ++l) {
			for (m = 0; m < cols; ++m) {
				answer_matrix[l][m] = 0.0;
				for (n = 0; n < cols; ++n) {
					//printf("You need %6.2f and %6.2f", matrix[l][n],
					//matrix_2[n][m]);
					answer_matrix[l][m] += matrix[l][n] * matrix_2[n][m];
					//printf("%6.2f ", answer_matrix[l][m]);
				}
				//printf("\n");
			}
		}

		tag = FROM_WORKER;
		MPI_Send(&start, 1, MPI_INT, MASTER, tag, MPI_COMM_WORLD);
		MPI_Send(&sent_rows, 1, MPI_INT, MASTER, tag, MPI_COMM_WORLD);
		pieces = sent_rows * cols;
		MPI_Send(&answer_matrix, pieces, MPI_DOUBLE, MASTER, tag,
				MPI_COMM_WORLD);

	}

	MPI_Finalize();
	return;
}

// Main
int main(int argc, char **argv) {

	// Do the matrix multiply
	multiplyMatrices(argc, argv);

	return EXIT_SUCCESS;
}

