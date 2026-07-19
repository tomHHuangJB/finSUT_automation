import { useQuery } from '@tanstack/react-query';
import { Alert, Box, Chip, CircularProgress, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import { listAccounts } from '../api/client';

export function AccountsPage() {
  const { data = [], isLoading, error } = useQuery({ queryKey: ['accounts'], queryFn: listAccounts });

  if (isLoading) {
    return <CircularProgress aria-label="Loading accounts" />;
  }

  if (error) {
    return <Alert severity="error">Could not load accounts.</Alert>;
  }

  return (
    <Box>
      <Typography variant="h5" component="h1" gutterBottom>
        Accounts
      </Typography>
      <TableContainer component={Paper} variant="outlined">
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Code</TableCell>
              <TableCell>Name</TableCell>
              <TableCell>Type</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="right">Cash</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((account) => (
              <TableRow key={account.id}>
                <TableCell>{account.accountCode}</TableCell>
                <TableCell>{account.accountName}</TableCell>
                <TableCell>{account.accountType}</TableCell>
                <TableCell>
                  <Chip size="small" label={account.status} color={account.status === 'ACTIVE' ? 'success' : 'default'} />
                </TableCell>
                <TableCell align="right">
                  {account.cashBalance.toLocaleString(undefined, { style: 'currency', currency: account.baseCurrency })}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}
